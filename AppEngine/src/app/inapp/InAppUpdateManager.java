package app.inapp;

import android.app.Activity;
import android.content.IntentSender;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import app.listener.InAppUpdateListener;

/**
 * Created by Meenu Singh on 2019-08-29.
 */
public class InAppUpdateManager {
    public static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private Activity activity;

    public InAppUpdateManager(Activity activity) {
        this.activity = activity;
    }

    /**
     * calling on 'onCreate' function..
     */
    public void checkForAppUpdate(final InAppUpdateListener inAppUpdateListener) {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(activity);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                System.out.println("InAppUpdateManager.onStateUpdate " + installState.installStatus());
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    unregisterInstallStateUpdListener();
                System.out.println("InAppUpdateManager.onStateUpdate InstallStatus.DOWNLOADED");
            }
        };

        // Checks that the platform will allow the specified type of update.

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                System.out.println("InAppUpdateManager.onSuccess " + appUpdateInfo.updateAvailability());
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    inAppUpdateListener.onUpdateAvailable();
                    // Request the update.
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                        // Before starting an update, register a listener for updates.
                        appUpdateManager.registerListener(installStateUpdatedListener);
                        // Start an update.
                        startAppUpdateFlexible(appUpdateInfo);
                    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Start an update.
                        startAppUpdateImmediate(appUpdateInfo);
                    }
                } else {
                    inAppUpdateListener.onUpdateNotAvailable();
                }
            }
        });
    }


    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    activity,
                    // Include a request code to later monitor this update request.
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    activity,
                    // Include a request code to later monitor this update request.
                    REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    public void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        //FLEXIBLE:
                        // If the update is downloaded but not installed,
                        // notify the user to complete the update.
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            //popupSnackbarForCompleteUpdateAndUnregister(drawerLayout);
                            unregisterInstallStateUpdListener();
                        }

                        //IMMEDIATE:
                        if (appUpdateInfo.updateAvailability()
                                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                            // If an in-app update is already running, resume the update.
                            startAppUpdateImmediate(appUpdateInfo);
                        }
                    }
                });
    }

    /**
     * Needed only for FLEXIBLE update
     */
    public void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }


//        /**
//     * Displays the snackbar notification and call to action.
//     * Needed only for Flexible app update
//     */
//    private void popupSnackbarForCompleteUpdateAndUnregister(DrawerLayout drawerLayout) {
//        Snackbar snackbar =
//                Snackbar.make(drawerLayout, "Update Complete", Snackbar.LENGTH_INDEFINITE);
//        snackbar.setAction("Restart", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                appUpdateManager.completeUpdate();
//            }
//        });
//        snackbar.setActionTextColor(activity.getResources().getColor(R.color.colorPrimary));
//        snackbar.show();
//
//        unregisterInstallStateUpdListener();
//    }

}

package com.techproof.appinstaller.model;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.techproof.appinstaller.Common.Constant;
import com.techproof.appinstaller.R;
import com.techproof.appinstaller.activity.SettingsActivity;
import com.techproof.appinstaller.activity.SplashActivityV3;
import com.techproof.appinstaller.utils.RecursiveFileObserver;

import app.fcm.MapperUtils;

/**
 * Created by Anon on 27,November,2019
 */
public class FileListenerService extends Service {

    private RecursiveFileObserver fileObserver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startMyOwnForeground();

            super.startForeground(101,newRunningNotification());
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileObserver = new RecursiveFileObserver(getPackageName(), getApplicationContext(), path);
        fileObserver.startWatching();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startMyOwnForeground();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fileObserver.stopWatching();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        } else {
            stopSelf();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "Apk Installer";
        String channelName = "Apk Installer";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.status_app_icon)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_NONE)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(0, notification);
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    private NotificationManager mNotificationManager;

    private Notification newRunningNotification() {
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);


        RemoteViews contentView = new RemoteViews(this.getPackageName(), R.layout.notification_app);

        Intent intent = new Intent(this, SplashActivityV3.class);
        intent.addCategory(this.getPackageName());
        PendingIntent pSplashLaunchIntent = PendingIntent.getActivity(this, Constant.NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent1 = new Intent(this, SplashActivityV3.class);
        intent.addCategory(this.getPackageName());
        intent1.putExtra(MapperUtils.keyType, "deeplink");
        intent1.putExtra(MapperUtils.keyValue, "_settings");
        PendingIntent pSettingLaunchIntent = PendingIntent.getActivity(this, Constant.NOTIFICATION_ID, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);

        contentView.setOnClickPendingIntent(R.id.linear, pSplashLaunchIntent);
        contentView.setOnClickPendingIntent(R.id.setting_button, pSettingLaunchIntent);

        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(this.getResources().getString(R.string.fcm_defaultSenderId),
                    "Chaneel name",
                    NotificationManager.IMPORTANCE_DEFAULT);

            mNotificationManager.createNotificationChannel(channel);

            Notification.Builder builder = new Notification.Builder(this,
                    this.getResources().getString(R.string.fcm_defaultSenderId));
            // .setContentTitle("Auto Download Service Enabled");

               // builder.setContentIntent(pcloseIntent);
            builder.setCustomContentView(contentView);
            builder.setSmallIcon(R.drawable.status_app_icon);
            notification = builder.build();

        } else {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this,
                            this.getResources().getString(R.string.fcm_defaultSenderId));
            //  .setContentTitle("Auto Download Service Enabled");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBuilder.setSmallIcon(R.drawable.status_app_icon);

            } else {
                mBuilder.setSmallIcon(R.drawable.app_icon);
            }

            mBuilder.setCustomContentView(contentView);

            notification = mBuilder.build();
        }

         //notification.contentIntent = pSplashLaunchIntent;

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        mNotificationManager.notify(Constant.NOTIFICATION_ID, notification);
        return notification;
    }
}

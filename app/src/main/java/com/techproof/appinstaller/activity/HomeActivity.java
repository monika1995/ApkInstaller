package com.techproof.appinstaller.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.techproof.appinstaller.Common.BaseClass;
import com.techproof.appinstaller.Common.Constant;
import com.techproof.appinstaller.R;
import com.techproof.appinstaller.adapter.ApkPermissionAdapter;
import com.techproof.appinstaller.adapter.HomePagerAdapter;
import com.techproof.appinstaller.fragment.ApksFragment;
import com.techproof.appinstaller.fragment.AppsFragment;
import com.techproof.appinstaller.model.ApkListModel;
import com.techproof.appinstaller.model.FileListenerService;
import com.techproof.appinstaller.utils.AppUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import app.adshandler.AHandler;
import app.fcm.MapperUtils;
import app.inapp.InAppUpdateManager;
import app.listener.InAppUpdateListener;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements InAppUpdateListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private HomePagerAdapter homePagerAdapter;
    AppsFragment appsFragment;
    ApksFragment apksFragment;
    String path;
    Dialog dialog;
    ApkListModel apkListModel;
    ArrayList<String> permissionsList;
    private InAppUpdateManager inAppUpdateManager;
    public static int count = 0;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    SearchView searchView;
    SearchManager searchManager;
    boolean apkHit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        permissionsList = new ArrayList<>();
        sharedPreferences = getSharedPreferences("My_Pref", 0);
        editor = sharedPreferences.edit();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);

        LinearLayout linearLayout = findViewById(R.id.adsbanner);
        linearLayout.addView(getBanner());

        setTabs();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                AHandler.getInstance().showFullAds(HomeActivity.this, false);

                if (tab.getPosition() == 0) {
                    AppUtils.onClickButtonFirebaseAnalytics(HomeActivity.this, Constant.FIREBASE_APPS);
                } else {
                    AppUtils.onClickButtonFirebaseAnalytics(HomeActivity.this, Constant.FIREBASE_APKS);
                    ApksFragment apksFragment = (ApksFragment) getSupportFragmentManager().getFragments().get(tab.getPosition());
                    if (apksFragment != null)
                        if(!apkHit) {
                            apksFragment.fetchAllApks();
                            apkHit = true;
                        }
                }

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Example Firebase App analytics
        // AppUtils.onClickButtonFirebaseAnalytics(this, Constant.FIREBASE_SETTING);

        inAppUpdateManager = new InAppUpdateManager(this);
        inAppUpdateManager.checkForAppUpdate(this);
        callingForMapper(this);

        System.out.println("AFragement---");

    }

    public void setInstallDialog(ApkListModel apkListModel) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_apkinstall);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        LinearLayout linearLayout = dialog.findViewById(R.id.adsbanner);
        linearLayout.addView(AHandler.getInstance().getBannerHeader(HomeActivity.this));
        //linearLayout.addView(getBanner());

        CardView cvPermission = dialog.findViewById(R.id.cardView_permissions);
        ImageView imgApp = dialog.findViewById(R.id.img_apk);
        TextView txtAppName = dialog.findViewById(R.id.txt_apkName);
        TextView txtVersionName = dialog.findViewById(R.id.txt_versionName);
        RecyclerView rvPermissions = dialog.findViewById(R.id.rv_permissions);
        TextView txtInstall = dialog.findViewById(R.id.btn_install);
        ImageView imgDropDown = dialog.findViewById(R.id.img_permission_arrow);
        TextView txtDownloadedDate = dialog.findViewById(R.id.txt_downloaded_date);
        rvPermissions.setVisibility(View.GONE);
        cvPermission.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPermissions.setLayoutManager(linearLayoutManager);
        ApkPermissionAdapter permissionAdapter = new ApkPermissionAdapter(this, permissionsList);
        rvPermissions.setAdapter(permissionAdapter);

        if (apkListModel != null) {
            if (apkListModel.getPackageInfo() != null) {
                if(apkListModel.getPackageInfo().applicationInfo!=null) {
                    String apkName = apkListModel.getPackageInfo().applicationInfo.loadLabel(getPackageManager()) + ".apk";
                    txtAppName.setText(apkName);
                }else {
                    txtAppName.setText(apkListModel.getPackageInfo().packageName);
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                txtDownloadedDate.setText(dateFormat.format(new Date(new File(apkListModel.getApkPath()).lastModified())));
                long fileSizeInMB = 0;
                try {
                    if (apkListModel.getPackageInfo().applicationInfo.sourceDir != null) {
                        long size = new File(apkListModel.getPackageInfo().applicationInfo.sourceDir).length();
                        long fileSizeInKB = size / 1024;
                        fileSizeInMB = fileSizeInKB / 1024;
                    } else {
                        long size = new File(apkListModel.getApkPath()).length();
                        long fileSizeInKB = size / 1024;
                        fileSizeInMB = fileSizeInKB / 1024;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String apkSize = fileSizeInMB + " MB | Version - " + apkListModel.getPackageInfo().versionName;
                txtVersionName.setText(apkSize);
                Drawable icon = getPackageManager().getApplicationIcon(apkListModel.getPackageInfo().applicationInfo);
                imgApp.setImageDrawable(icon);
            }

            permissionsList.clear();
            permissionsList.addAll(new BaseClass().getListOfPermissions(this, apkListModel.getApkPath()));
            permissionAdapter.notifyDataSetChanged();

            txtInstall.setOnClickListener(view -> {
                installApk(new File(apkListModel.getApkPath()));
                dialog.dismiss();
            });

            imgDropDown.setOnClickListener(view -> {
                if (rvPermissions.getVisibility() == View.VISIBLE) {
                    rvPermissions.setVisibility(View.GONE);
                    cvPermission.setVisibility(View.GONE);
                    imgDropDown.setRotation(0);
                } else {
                    rvPermissions.setVisibility(View.VISIBLE);
                    cvPermission.setVisibility(View.VISIBLE);
                    imgDropDown.setRotation(180);
                }
            });
        }
    }

    public void installApk(File file) {
        try {
            if (file.exists()) {
                String[] fileNameArray = file.getName().split(Pattern.quote("."));
                if (fileNameArray[fileNameArray.length - 1].equals("apk")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri downloaded_apk = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                        Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(downloaded_apk,
                                "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, downloaded_apk);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file),
                                "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_more) {

            startActivity(new Intent(HomeActivity.this, MoreActivity.class));

            AHandler.getInstance().showFullAds(HomeActivity.this, false);

            return true;
        } else if (item.getItemId() == R.id.action_search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (viewPager.getCurrentItem() == 0) {
                        appsFragment = (AppsFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                        if (appsFragment != null)
                            appsFragment.setAppFilter(newText);
                    } else if (viewPager.getCurrentItem() == 1) {
                        apksFragment = (ApksFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                        if (apksFragment != null)
                            apksFragment.setApkFilter(newText);
                    }
                    return false;
                }
            });
        } else if (item.getItemId() == R.id.action_delete) {
            if (viewPager.getCurrentItem() == 0) {
                AppsFragment appsFragment = (AppsFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                if (appsFragment != null)
                    appsFragment.deleteApp();
                AppUtils.onClickButtonFirebaseAnalytics(HomeActivity.this, Constant.FIREBASE_APP_DELETE);
            } else if (viewPager.getCurrentItem() == 1) {
                ApksFragment apksFragment = (ApksFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                if (apksFragment != null)
                    apksFragment.deleteApk();
                AppUtils.onClickButtonFirebaseAnalytics(HomeActivity.this, Constant.FIREBASE_APK_DELETE);
            }

            AHandler.getInstance().showFullAds(HomeActivity.this, false);

        } else if (item.getItemId() == R.id.action_share) {
            if (viewPager.getCurrentItem() == 0) {
                AppsFragment appsFragment = (AppsFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                if (appsFragment != null)
                    appsFragment.shareApp();
            } else if (viewPager.getCurrentItem() == 1) {
                ApksFragment apksFragment = (ApksFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                if (apksFragment != null)
                    apksFragment.shareApk();
            }
        }
        return false;
    }

    public void setTabs() {
        tabs.addTab(tabs.newTab().setText("Apps"));
        tabs.addTab(tabs.newTab().setText("Apks"));
        homePagerAdapter = new HomePagerAdapter(this, getSupportFragmentManager(), tabs.getTabCount());
        new Handler().postDelayed(() -> viewPager.setAdapter(homePagerAdapter),2000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        inAppUpdateManager.checkNewAppVersionState();

        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcastReceiver,
                new IntentFilter("APK file create"));

        path = sharedPreferences.getString(Constant.APK_PATH, "");
        if (!path.equals("")) {
            File file = new File(path);
            if (file.exists()) {
                apkListModel = new ApkListModel();
                apkListModel.setApkPath(path);
                apkListModel.setApkName(new File(path).getName());
                PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_META_DATA);
                packageInfo.applicationInfo.sourceDir = path;
                packageInfo.applicationInfo.publicSourceDir = path;
                apkListModel.setPackageInfo(packageInfo);
                setInstallDialog(apkListModel);

                editor.putString(Constant.APK_PATH, "");
                editor.commit();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadcastReceiver);
    }

    public void changesInFile() {
        if (viewPager.getCurrentItem() == 1) {
            ApksFragment apksFragment = (ApksFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
            apksFragment.refreshpage();
        }
    }

    private final BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, android.content.Intent intent) {
            changesInFile();
        }
    };

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        if (count == 0) {
            toolbar.getMenu().findItem(R.id.action_search).setVisible(true);
            toolbar.getMenu().findItem(R.id.action_more).setVisible(true);
            toolbar.getMenu().findItem(R.id.action_delete).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_share).setVisible(false);

            if (viewPager.getCurrentItem() == 0) {
                AppsFragment appsFragment = (AppsFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                if (appsFragment != null)
                    appsFragment.refreshAdapter();
            } else {
                ApksFragment apksFragment = (ApksFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                if (apksFragment != null)
                    apksFragment.refreshAdapter();
            }
            count++;
        } else {
            AHandler.getInstance().showExitPrompt(this);
            AHandler.getInstance().v2ManageAppExit(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case InAppUpdateManager.REQ_CODE_VERSION_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    inAppUpdateManager.unregisterInstallStateUpdListener();
                    onUpdateNotAvailable();
                }
                break;
        }
    }

    @Override
    public void onUpdateAvailable() {

    }

    @Override
    public void onUpdateNotAvailable() {
        AHandler.getInstance().v2CallonAppLaunch(this);

    }

    private void callingForMapper(Activity context) {
        Intent intent = context.getIntent();
        String type = intent.getStringExtra(MapperUtils.keyType);
        String value = intent.getStringExtra(MapperUtils.keyValue);
        System.out.println("AHandler.callingForMapper " + type + " " + value);
        try {
            if (type != null && value != null) {
                if (type.equalsIgnoreCase("url")) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(ContextCompat.getColor(context, app.pnd.adshandler.R.color.colorPrimary));
                    builder.addDefaultShareMenuItem();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(value));

                } else if (type.equalsIgnoreCase("deeplink")) {
                    switch (value) {

                        case MapperUtils.LAUNCH_APP_SETTING:
                            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                            break;

                        case MapperUtils.LAUNCH_APK:
                            tabs.getTabAt(1).select();
                            break;

                    }
                }
            }
        } catch (Exception e) {
            System.out.println("AHandler.callingForMapper excep " + e.getMessage());
        }
    }
}

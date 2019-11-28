package com.q4u.apkinstaller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.q4u.apkinstaller.Common.BaseClass;
import com.q4u.apkinstaller.Common.Constant;
import com.q4u.apkinstaller.R;
import com.q4u.apkinstaller.adapter.ApkPermissionAdapter;
import com.q4u.apkinstaller.adapter.HomePagerAdapter;
import com.q4u.apkinstaller.fragment.ApksFragment;
import com.q4u.apkinstaller.fragment.AppsFragment;
import com.q4u.apkinstaller.model.ApkListModel;
import com.q4u.apkinstaller.model.FileListenerService;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    HomePagerAdapter homePagerAdapter;
    AppsFragment appsFragment;
    ApksFragment apksFragment;
    String path;
    Dialog dialog;
    ApkListModel apkListModel;
    ArrayList<String> permissionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        permissionsList = new ArrayList<>();

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        setTabs();

        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //refreshTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void refreshTab(int position) {
        switch (position) {
            case 0:
                AppsFragment appsFragment = (AppsFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                appsFragment.refreshpage();
                break;
            case 1:
                ApksFragment apksFragment = (ApksFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                apksFragment.refreshpage();
                break;
        }
    }

    public void setInstallDialog(ApkListModel apkListModel)
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_apkinstall);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        ImageView imgApp = dialog.findViewById(R.id.img_apk);
        TextView txtAppName = dialog.findViewById(R.id.txt_apkName);
        TextView txtVersionName = dialog.findViewById(R.id.txt_versionName);
        RecyclerView rvPermissions = dialog.findViewById(R.id.rv_permissions);
        TextView txtInstall = dialog.findViewById(R.id.btn_install);
        ImageView imgDropDown = dialog.findViewById(R.id.img_permission_arrow);
        TextView txtDownloadedDate = dialog.findViewById(R.id.txt_downloaded_date);
        rvPermissions.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvPermissions.setLayoutManager(linearLayoutManager);
        if(apkListModel!=null) {
            txtAppName.setText(apkListModel.getPackageInfo().packageName);
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

            permissionsList.clear();
            permissionsList.addAll(new BaseClass().getListOfPermissions(this, apkListModel.getApkPath()));
            ApkPermissionAdapter permissionAdapter = new ApkPermissionAdapter(this, permissionsList);
            rvPermissions.setAdapter(permissionAdapter);

            txtInstall.setOnClickListener(view -> {
                installApk(new File(apkListModel.getApkPath()));
                dialog.dismiss();
            });

            imgDropDown.setOnClickListener(view -> {
                if (rvPermissions.getVisibility() == View.VISIBLE) {
                    rvPermissions.setVisibility(View.GONE);
                    imgDropDown.setRotation(0);
                } else {
                    rvPermissions.setVisibility(View.VISIBLE);
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
        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_more)
        {
            startActivity(new Intent(HomeActivity.this,MoreActivity.class));
            return true;
        }else if(item.getItemId()==R.id.action_search)
        {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(viewPager.getCurrentItem()==0)
                    {
                        appsFragment = (AppsFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                        if(appsFragment!=null)
                            appsFragment.setAppFilter(newText);
                    }else if(viewPager.getCurrentItem()==1)
                    {
                        apksFragment = (ApksFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                        if(apksFragment!=null)
                            apksFragment.setApkFilter(newText);
                    }
                    return false;
                }
            });
        }else if(item.getItemId()==R.id.action_delete)
        {
            if(viewPager.getCurrentItem()==0)
            {
                AppsFragment appsFragment = (AppsFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                if(appsFragment!=null)
                    appsFragment.deleteApp();
            }else if(viewPager.getCurrentItem()==1)
            {
                ApksFragment apksFragment = (ApksFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                if(apksFragment!=null)
                    apksFragment.deleteApk();
            }
        }else if(item.getItemId()==R.id.action_share)
        {
            if(viewPager.getCurrentItem()==0)
            {
                AppsFragment appsFragment = (AppsFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                if(appsFragment!=null)
                    appsFragment.shareApp();
            }else if(viewPager.getCurrentItem()==1)
            {
                ApksFragment apksFragment = (ApksFragment) getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
                if(apksFragment!=null)
                apksFragment.shareApk();
            }
        }
        return false;
    }

    public void setTabs()
    {
        tabs.addTab(tabs.newTab().setText("Apps"));
        tabs.addTab(tabs.newTab().setText("Apks"));
        homePagerAdapter = new HomePagerAdapter(this,getSupportFragmentManager(),tabs.getTabCount());
        viewPager.setAdapter(homePagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcastReceiver,
                new IntentFilter("APK file create"));

        path = getIntent().getStringExtra(Constant.APK_PATH);
        if(path!=null){
            apkListModel = new ApkListModel();
            apkListModel.setApkPath(path);
            apkListModel.setApkName(new File(path).getName());
            PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(path,PackageManager.GET_META_DATA);
            apkListModel.setPackageInfo(packageInfo);
            setInstallDialog(apkListModel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadcastReceiver);
    }

    public void changesInFile()
    {
        refreshTab(viewPager.getCurrentItem());
    }

    private final BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, android.content.Intent intent) {
            String path = intent.getStringExtra("path");
            changesInFile();
        }
    };

}

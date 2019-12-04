package com.techproof.appinstaller.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.techproof.appinstaller.Common.Constant;
import com.techproof.appinstaller.Common.DebugLogger;
import com.techproof.appinstaller.R;
import com.techproof.appinstaller.model.FileListenerService;

import app.adshandler.AHandler;
import app.campaign.CampaignHandler;
import app.fcm.GCMPreferences;
import app.fcm.MapperUtils;
import app.listener.OnCacheFullAdLoaded;
import app.serviceprovider.Utils;

/**
 * Created by quantum4u1 on 14/04/18.
 */


public class SplashActivityV3 extends AppCompatActivity {
    private GCMPreferences mPreference;
    RelativeLayout layoutStart;
    private Handler h;
    private boolean appLaunch = false;

    String path;
    boolean isFromNotification = false;
    private static final int PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_firebase);

        appLaunch = false;

        ImageView imageView = findViewById(R.id.imageView);


        isFromNotification = getIntent().getBooleanExtra(Constant.FROM_NOTIFICATION,false);
        path = getIntent().getStringExtra(Constant.PATH);

        AHandler.getInstance().v2CallOnSplash(this, new OnCacheFullAdLoaded() {
            @Override
            public void onCacheFullAd() {
                if (!mPreference.isFirsttime()) {
                    launchApp();

                    try {
                        if (h != null)
                            h.removeCallbacks(r);
                    } catch (Exception e) {
                        System.out.println("exception splash 1" + " " + e);
                    }
                }
            }
        });

        CampaignHandler.getInstance().initCampaign(this, null);
        mPreference = new GCMPreferences(this);


        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_splash);
        layoutStart = findViewById(R.id.layoutStart);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mPreference.isFirsttime()) {
                    layoutStart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        layoutStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchApp();
                mPreference.setFirstTime(false);
            }
        });

        if (mPreference.isFirsttime()) {
            //layoutStart.setVisibility(View.VISIBLE);
            imageView.setAnimation(animation);
        } else {
            findViewById(R.id.imageView).setVisibility(View.GONE);
            ImageView logo = findViewById(R.id.Logo);
            logo.setVisibility(View.VISIBLE);
            logo.setAnimation(animation);

            layoutStart.setVisibility(View.GONE);

            h = new Handler();
            h.postDelayed(r, 6000);
        }

        LinearLayout layout_tnc = findViewById(R.id.layout_tnc);
        new Utils().showPrivacyPolicy(this, layout_tnc, mPreference.isFirsttime());

        LinearLayout adsbanner = findViewById(R.id.adsbanner);
        adsbanner.addView(AHandler.getInstance().getBannerHeader(this));

    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            launchApp();
        }
    };

    private void launchApp() {
        if(checkPermission()){
            if (!appLaunch) {
                appLaunch = true;
                appLaunch(HomeActivity.class);
                finish();
            }
        }else {
            requestPermission();
        }
    }

    private void appLaunch(Class<?> cls) {
        Intent intent = getIntent();
        String type = intent.getStringExtra(MapperUtils.keyType);
        String value = intent.getStringExtra(MapperUtils.keyValue);

        try {
            if (type != null && value != null) {
                launchAppWithMapper(cls, type, value);
            } else {
                startActivity(new Intent(SplashActivityV3.this, cls)
                );
            }
        } catch (Exception e) {
        }

    }

    private void launchAppWithMapper(Class<?> cls, String type, String value) {
        startActivity(new Intent(this, cls)
                .putExtra(MapperUtils.keyType, type)
                .putExtra(MapperUtils.keyValue, value));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AHandler.getInstance().onAHandlerDestroy();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(SplashActivityV3.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(SplashActivityV3.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
            DebugLogger.d("Permission grant--3");
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivityV3.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(SplashActivityV3.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            DebugLogger.d("Permission_storage");
            Toast.makeText(SplashActivityV3.this, "Write External Storage permission allows us to read files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            DebugLogger.d("Permission_storage");
            ActivityCompat.requestPermissions(SplashActivityV3.this, new String[]
                    {android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DebugLogger.d("Permission grant 1---");
                    if (!appLaunch) {
                        appLaunch = true;
                        appLaunch(HomeActivity.class);
                        finish();

                    }
                } else {
                    DebugLogger.d("Permission Denied, You cannot use local drive .");
                    requestPermission();
                }
                break;
        }
    }

}

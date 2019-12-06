package com.techproof.appinstaller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.techproof.appinstaller.activity.HomeActivity;
import com.techproof.appinstaller.activity.SplashActivityV3;

import app.adshandler.AHandler;
import app.fcm.MapperUtils;

/**
 * Created by Meenu Singh on 13-12-2017.
 */
public class MapperActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Window window = this.getWindow();
//        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        AHandler.getInstance().v2CallOnBGLaunch(this);

        intent = getIntent();
        String type = intent.getStringExtra(MapperUtils.keyType);
        String value = intent.getStringExtra(MapperUtils.keyValue);

        System.out.println("0643 key value" + " " + value);

        if (type != null && value != null) {
            if (type.equalsIgnoreCase("url")) {
                launchAppWithMapper(type, value);

            } else if (type.equalsIgnoreCase("deeplink")) {
                handleValue(type, value);

            }
            else {
                this.finish();
            }
        } else {
            this.finish();
        }
    }


    private void handleValue(String type, String value) {
        System.out.println("0643 key value111" + " " + value);

        try {
            switch (value) {
                //Engine Mapping
                case MapperUtils.gcmAppLaunch:
                    //Remember to add here your LauncherClass.
                      startActivity(new Intent(this, HomeActivity.class));
                      break;
                case MapperUtils.LAUNCH_SPLASH:
                    this.finish();
                    //Remember to add here your LauncherClass.
                    startActivity(new Intent(this, SplashActivityV3.class));
                    break;

                case MapperUtils.gcmMoreApp:
                    this.finish();
                    //Remember to add here your MoreAppClass.
                    launchAppWithMapper(type, MapperUtils.gcmMoreApp);
                    break;

                case MapperUtils.gcmFeedbackApp:
                    this.finish();
                    //Remember to add here your FeedbackClass.
                    launchAppWithMapper(type, MapperUtils.gcmFeedbackApp);
                    break;

                case MapperUtils.gcmRateApp:
                    this.finish();
                    //Remember to add here your RareAppClass.
                    launchAppWithMapper(type, MapperUtils.gcmRateApp);
                    break;

                case MapperUtils.gcmShareApp:
                    this.finish();
                    //Remember to add here your ShareAppClass.
                    launchAppWithMapper(type, MapperUtils.gcmShareApp);
                    break;

                case MapperUtils.gcmRemoveAds:
                    this.finish();
                    //Remember to add here your RemoveAdClass.
                    launchAppWithMapper(type, MapperUtils.gcmRemoveAds);
                    break;

                case MapperUtils.gcmForceAppUpdate:
                    this.finish();
                    launchAppWithMapper(type, MapperUtils.gcmForceAppUpdate);
                    break;

                //App Mapping
                case MapperUtils.LAUNCH_SETTINGS:
                    launchAppWithMapper(type,MapperUtils.LAUNCH_SETTINGS);
                    break;

                case MapperUtils.LAUNCH_APP_SETTING:
                    launchAppWithMapper(type,MapperUtils.LAUNCH_APP_SETTING);
                    break;

                case MapperUtils.LAUNCH_APK:
                    launchAppWithMapper(type,MapperUtils.LAUNCH_APK);
                    break;

                default:
                    startActivity(new Intent(this, SplashActivityV3.class));
                    break;
            }

        } catch (Exception e) {
            this.finish();
            System.out.println("0643 key value222" + " " + value);

            startActivity(new Intent(this, SplashActivityV3.class));
        }

    }

    private void launchAppWithMapper(String type, String value) {
        startActivity(new Intent(this, SplashActivityV3.class)
                .putExtra(MapperUtils.keyType, type)
                .putExtra(MapperUtils.keyValue, value)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

}

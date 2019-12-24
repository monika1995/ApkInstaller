package com.techproof.appinstaller.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.techproof.appinstaller.Common.Constant;
import com.techproof.appinstaller.R;
import com.techproof.appinstaller.model.FileListenerService;
import com.techproof.appinstaller.utils.AppUtils;

import org.jsoup.Connection;

import app.adshandler.AHandler;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar_more)
    Toolbar toolbarMore;
    @BindView(R.id.switch_notification)
    SwitchCompat switchNotification;
    SharedPreferences.Editor sharedPreferences;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getApplicationContext().getSharedPreferences(Constant.PREF_NAME, 0).edit();
        preferences = getApplicationContext().getSharedPreferences(Constant.PREF_NAME, 0);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMore);

        toolbarMore.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        switchNotification.setChecked(preferences.getBoolean(Constant.IS_NOTIFICATION,true));
        sharedPreferences.putBoolean(Constant.IS_NOTIFICATION,switchNotification.isChecked());

        if(switchNotification.isChecked())
        {
            if (!AppUtils.isMyServiceRunning(FileListenerService.class, this)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this.startForegroundService(new Intent(this, FileListenerService.class));
                } else {
                    this.startService(new Intent(this, FileListenerService.class));
                }
            }
        }else{
            if (AppUtils.isMyServiceRunning(FileListenerService.class, this)) {
                this.stopService(new Intent(this,FileListenerService.class));
            }
        }

        switchNotification.setOnCheckedChangeListener((compoundButton, b) -> {
            if(switchNotification.isChecked())
            {
               sharedPreferences.putBoolean(Constant.IS_NOTIFICATION,true);
                if (!AppUtils.isMyServiceRunning(FileListenerService.class, this)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        this.startForegroundService(new Intent(this, FileListenerService.class));
                    } else {
                        this.startService(new Intent(this, FileListenerService.class));
                    }
                }
            }else{
                sharedPreferences.putBoolean(Constant.IS_NOTIFICATION,false);

                this.stopService(new Intent(this,FileListenerService.class));
            }
            sharedPreferences.apply();
        });

        LinearLayout linearLayout = findViewById(R.id.adsbanner);
        //linearLayout.addView(AHandler.getInstance().getBannerHeader(this));
        linearLayout.addView(getBanner());
    }
}

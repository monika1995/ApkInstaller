package com.techproof.appinstaller.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.techproof.appinstaller.Common.Constant;
import com.techproof.appinstaller.R;

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

        switchNotification.setChecked(preferences.getBoolean(Constant.IS_NOTIFICATION,false));
        sharedPreferences.putBoolean(Constant.IS_NOTIFICATION,switchNotification.isChecked());

        switchNotification.setOnCheckedChangeListener((compoundButton, b) -> {
            if(switchNotification.isChecked())
            {
               sharedPreferences.putBoolean(Constant.IS_NOTIFICATION,true);
            }else{
                sharedPreferences.putBoolean(Constant.IS_NOTIFICATION,false);
            }
            sharedPreferences.apply();
        });

        LinearLayout linearLayout = findViewById(R.id.adsbanner);
        //linearLayout.addView(AHandler.getInstance().getBannerHeader(this));
        linearLayout.addView(getBanner());
    }
}

package com.techproof.appinstaller.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.techproof.appinstaller.Common.Constant;
import com.techproof.appinstaller.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_more)
    Toolbar toolbarMore;
    @BindView(R.id.switch_notification)
    SwitchCompat switchNotification;
    SharedPreferences.Editor sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getApplicationContext().getSharedPreferences(Constant.PREF_NAME, 0).edit();
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMore);

        toolbarMore.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

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
    }
}

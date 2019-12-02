package com.techproof.appinstaller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.techproof.appinstaller.Common.Constant;
import com.techproof.appinstaller.R;
import com.techproof.appinstaller.utils.AppUtils;

import app.adshandler.AHandler;
import app.adshandler.PromptHander;
import app.serviceprovider.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_more)
    Toolbar toolbarMore;

    @BindView(R.id.txt_rateUs)
    TextView textViewRateus;

    @BindView(R.id.txt_moreApps)
    TextView textViewMore;

    @BindView(R.id.txt_shareApp)
    TextView textViewshareApp;

    @BindView(R.id.txt_feedback)
    TextView textViewFeedback;

    @BindView(R.id.txt_aboutUs)
    TextView textViewAboutUs;

    @BindView(R.id.txt_settings)
    TextView textViewSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        ButterKnife.bind(this);
        setSupportActionBar(toolbarMore);

        toolbarMore.setNavigationOnClickListener(view -> onBackPressed());
        textViewRateus.setOnClickListener(this);
        textViewMore.setOnClickListener(this);
        textViewshareApp.setOnClickListener(this);
        textViewFeedback.setOnClickListener(this);
        textViewAboutUs.setOnClickListener(this);
        textViewSetting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.txt_rateUs:
                new PromptHander().rateUsDialog(this);
                AHandler.getInstance().showFullAds(this,false);
                AppUtils.onClickButtonFirebaseAnalytics(MoreActivity.this, Constant.FIREBASE_RATEUS);
                break;

            case R.id.txt_moreApps:
                new Utils().moreApps(this);
                AppUtils.onClickButtonFirebaseAnalytics(MoreActivity.this, Constant.FIREBASE_MOREAPPS);
                break;

            case R.id.txt_shareApp:
                new Utils().shareUrl(this);
                AHandler.getInstance().showFullAds(this,false);
                AppUtils.onClickButtonFirebaseAnalytics(MoreActivity.this, Constant.FIREBASE_SHAREAPPS);
                break;

            case R.id.txt_feedback:
                new Utils().sendFeedback(this);
                AppUtils.onClickButtonFirebaseAnalytics(MoreActivity.this, Constant.FIREBASE_FEEDBACK);
                break;

            case R.id.txt_aboutUs:
                AHandler.getInstance().showAboutUs(this);
                AppUtils.onClickButtonFirebaseAnalytics(MoreActivity.this, Constant.FIREBASE_ABOUTUS);
                break;

            case R.id.txt_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                AppUtils.onClickButtonFirebaseAnalytics(MoreActivity.this, Constant.FIREBASE_SETTING);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

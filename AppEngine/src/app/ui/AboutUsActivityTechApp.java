package app.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import app.PrintLog;
import app.adshandler.AHandler;
import app.pnd.adshandler.R;
import app.server.v2.Slave;
import app.serviceprovider.Utils;

/**
 * Created by Meenu Singh on 2019-11-28.
 */
public class AboutUsActivityTechApp extends AppCompatActivity {
    private int i = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_techapp);

        Toolbar toolbar = findViewById(R.id.mToolBar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.title_toolbar_about_us));
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/billing_regular.ttf");

        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i == 10) {
                    i = 0;
                    ShowAssetValueDialog showAssetValueDialog = new ShowAssetValueDialog(AboutUsActivityTechApp.this, new ShowAssetValueDialog.OnSelecteShowValueCallBack() {
                        @Override
                        public void onShowValueSelected(int position) {
                            Intent myIntent = new Intent(AboutUsActivityTechApp.this, PrintActivity.class);
                            myIntent.putExtra(Utils.SHOW_VALUE, position);
                            startActivity(myIntent);

                        }
                    });
                    showAssetValueDialog.setCancelable(false);
                    showAssetValueDialog.show();

                }
            }
        });

        TextView tv_appversion = findViewById(R.id.tv_appversion);
        TextView website = findViewById(R.id.website);
        TextView all_apps = findViewById(R.id.all_apps);
        TextView term_service = findViewById(R.id.term_service);
        TextView privacy_policy = findViewById(R.id.privacy_policy);
        TextView mail_us = findViewById(R.id.mail_us);
        TextView appName = findViewById(R.id.appName);

        tv_appversion.setTypeface(typeface);
        website.setTypeface(typeface);
        all_apps.setTypeface(typeface);
        term_service.setTypeface(typeface);
        privacy_policy.setTypeface(typeface);
        mail_us.setTypeface(typeface);
        appName.setTypeface(typeface);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tv_appversion.setText("Ver. " + version);
        } catch (Exception e) {
            e.printStackTrace();
            PrintLog.print("exception in checking app version");
        }

        RelativeLayout rl_website = findViewById(R.id.rl_website);
        RelativeLayout rl_our_apps = findViewById(R.id.rl_our_apps);
        RelativeLayout rl_terms_of_service = findViewById(R.id.rl_terms_of_service);
        RelativeLayout rl_privacy_policy = findViewById(R.id.rl_privacy_policy);
        RelativeLayout rl_mail_us = findViewById(R.id.rl_mail_us);

        rl_website.setOnClickListener(mOnClickListener);
        rl_our_apps.setOnClickListener(mOnClickListener);
        rl_terms_of_service.setOnClickListener(mOnClickListener);
        rl_privacy_policy.setOnClickListener(mOnClickListener);
        rl_mail_us.setOnClickListener(mOnClickListener);

        LinearLayout linearLayout = findViewById(R.id.adsbanner);
        linearLayout.addView(AHandler.getInstance().getBannerHeader(AboutUsActivityTechApp.this));
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.rl_website) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_WEBSITELINK)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (view.getId() == R.id.rl_our_apps) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_OURAPP)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (view.getId() == R.id.rl_terms_of_service) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_TERM_AND_COND)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (view.getId() == R.id.rl_privacy_policy) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_PRIVACYPOLICY)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (view.getId() == R.id.rl_mail_us) {
                new Utils().sendFeedback(AboutUsActivityTechApp.this);
            }
        }


    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

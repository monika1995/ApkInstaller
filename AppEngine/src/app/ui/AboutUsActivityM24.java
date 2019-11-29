package app.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import app.pnd.adshandler.R;
import app.server.v2.Slave;
import app.serviceprovider.Utils;

/**
 * Created by Anon on 08,January,2019
 */
public class AboutUsActivityM24 extends AppCompatActivity {
    private int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_m24);

        Toolbar toolbar = findViewById(R.id.mToolBar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.title_toolbar_about_us));
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (Slave.ETC_5.equalsIgnoreCase("1")) {
            findViewById(R.id.follow_layout).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i == 10) {
                    i = 0;
                    ShowAssetValueDialog showAssetValueDialog = new ShowAssetValueDialog(AboutUsActivityM24.this, new ShowAssetValueDialog.OnSelecteShowValueCallBack() {
                        @Override
                        public void onShowValueSelected(int position) {
                            Intent myIntent = new Intent(AboutUsActivityM24.this, PrintActivity.class);
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
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tv_appversion.setText("Ver. " + version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinearLayout rl_mail_us = findViewById(R.id.rl_mail_us);
        rl_mail_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Utils().sendFeedback(AboutUsActivityM24.this);
            }
        });

        LinearLayout rl_website = findViewById(R.id.rl_website);
        LinearLayout rl_our_apps = findViewById(R.id.rl_our_apps);
        LinearLayout rl_terms_of_service = findViewById(R.id.rl_terms_of_service);
        LinearLayout rl_privacy_policy = findViewById(R.id.rl_privacy_policy);

        ImageView iv_fb = findViewById(R.id.iv_fb);

        ImageView iv_twitter = findViewById(R.id.iv_twitter);
        ImageView iv_instagram = findViewById(R.id.iv_instagram);

        iv_fb.setOnClickListener(mOnClickListener);
        iv_twitter.setOnClickListener(mOnClickListener);
        iv_instagram.setOnClickListener(mOnClickListener);

        rl_website.setOnClickListener(mOnClickListener);
        rl_our_apps.setOnClickListener(mOnClickListener);
        rl_terms_of_service.setOnClickListener(mOnClickListener);
        rl_privacy_policy.setOnClickListener(mOnClickListener);
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
            } else if (view.getId() == R.id.iv_fb) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_FACEBOOK)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (view.getId() == R.id.iv_insta) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_INSTA)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (view.getId() == R.id.iv_twitter) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_TWITTER)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (view.getId() == R.id.iv_instagram) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_INSTA)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

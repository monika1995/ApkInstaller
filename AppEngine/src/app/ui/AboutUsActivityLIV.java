package app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.PrintLog;
import app.adshandler.PromptHander;
import app.pnd.adshandler.R;
import app.server.v2.Slave;
import app.serviceprovider.Utils;


/**
 * Created by quantum4u1 on 15/03/18.
 */

public class AboutUsActivityLIV extends Activity {
    private int i = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_liv);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/billing_regular.ttf");

        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                if (i == 10) {
                    i = 0;
                    ShowAssetValueDialog showAssetValueDialog = new ShowAssetValueDialog(AboutUsActivityLIV.this, new ShowAssetValueDialog.OnSelecteShowValueCallBack() {
                        @Override
                        public void onShowValueSelected(int position) {
                            Intent myIntent = new Intent(AboutUsActivityLIV.this, PrintActivity.class);
                            myIntent.putExtra(Utils.SHOW_VALUE, position);
                            startActivity(myIntent);

                        }
                    });
                    showAssetValueDialog.setCancelable(false);
                    showAssetValueDialog.show();

                }
            }
        });
        TextView tv_query = findViewById(R.id.tv_query);
        TextView tv_appversion = findViewById(R.id.tv_appversion);
        TextView feedback = findViewById(R.id.feedback);
        TextView rateus = findViewById(R.id.rateus);
        TextView website = findViewById(R.id.website);
        TextView all_apps = findViewById(R.id.all_apps);
        TextView term_service = findViewById(R.id.term_service);
        TextView privacy_policy = findViewById(R.id.privacy_policy);
        TextView appName = findViewById(R.id.appName);

        tv_query.setTypeface(typeface);
        tv_appversion.setTypeface(typeface);
        feedback.setTypeface(typeface);
        rateus.setTypeface(typeface);
        website.setTypeface(typeface);
        all_apps.setTypeface(typeface);
        term_service.setTypeface(typeface);
        privacy_policy.setTypeface(typeface);
        appName.setTypeface(typeface);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tv_appversion.setText("Ver. " + version);
        } catch (Exception e) {
            e.printStackTrace();
            PrintLog.print("exception in checking app version");
        }

        String myString = "if any issues/Query please contact us ";
        int i1 = myString.indexOf("c");
        int i2 = myString.indexOf("us");
        tv_query.setMovementMethod(LinkMovementMethod.getInstance());
        tv_query.setText(myString, TextView.BufferType.SPANNABLE);
        Spannable mySpannable = (Spannable) tv_query.getText();
        ClickableSpan myClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                new Utils().sendFeedback(AboutUsActivityLIV.this);
            }
        };
        mySpannable.setSpan(myClickableSpan, i1, i2 + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        RelativeLayout rl_website = findViewById(R.id.rl_website);
        RelativeLayout rl_our_apps = findViewById(R.id.rl_our_apps);
        RelativeLayout rl_terms_of_service = findViewById(R.id.rl_terms_of_service);
        RelativeLayout rl_privacy_policy = findViewById(R.id.rl_privacy_policy);
        RelativeLayout rl_feedback = findViewById(R.id.rl_feedback);
        RelativeLayout rl_rateus = findViewById(R.id.rl_rateus);

        ImageView iv_fb = findViewById(R.id.iv_fb);
        ImageView iv_insta = findViewById(R.id.iv_insta);
        ImageView iv_twitter = findViewById(R.id.iv_twitter);

        iv_fb.setOnClickListener(mOnClickListener);
        iv_insta.setOnClickListener(mOnClickListener);
        iv_twitter.setOnClickListener(mOnClickListener);

        rl_website.setOnClickListener(mOnClickListener);
        rl_our_apps.setOnClickListener(mOnClickListener);
        rl_terms_of_service.setOnClickListener(mOnClickListener);
        rl_privacy_policy.setOnClickListener(mOnClickListener);
        rl_feedback.setOnClickListener(mOnClickListener);
        rl_rateus.setOnClickListener(mOnClickListener);
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
            } else if (view.getId() == R.id.rl_feedback) {
                new Utils().sendFeedback(AboutUsActivityLIV.this);

            } else if (view.getId() == R.id.rl_rateus) {
                new PromptHander().rateUsDialog(AboutUsActivityLIV.this);
            }
        }


    };
}
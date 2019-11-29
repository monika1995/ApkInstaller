package app.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.PrintLog;
import app.adshandler.AHandler;
import app.pnd.adshandler.R;
import app.server.v2.DataHubConstant;
import app.server.v2.Slave;
import app.serviceprovider.Utils;

import static app.serviceprovider.Utils.getStringtoInt;

public class ExitAdsActivity extends AppCompatActivity {

    private TextView feature_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exitads);
        RelativeLayout ads_layout = findViewById(R.id.ads_layout);
        feature_text = findViewById(R.id.feature_text);
        feature_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Log.d("ExitAdsActivity", "Hello onCreate rrr001 " + " " + Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT);
        if (Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT.equals("true")) {
            if (!Slave.hasPurchased(this)) {
                if (Utils.isNetworkConnected(this)) {
                    ads_layout.setVisibility(View.VISIBLE);
                   // Log.d("ExitAdsActivity", "Hello onCreate rrr001 " + " ");
                } else {
                //    Log.d("ExitAdsActivity", "Hello onCreate rrr002 " + " ");
                }

                //load rectangle ad here
                LinearLayout linearLayout = findViewById(R.id.adsNativeExit);
                linearLayout.addView(AHandler.getInstance().getBannerRectangle(this));
                //AHandler.getInstance().getNewBannerRectangle(linearLayout);
            }

        }

        final RelativeLayout later = findViewById(R.id.later);
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                new Utils().moreApps(ExitAdsActivity.this);
            }
        });


        RelativeLayout leave = findViewById(R.id.leave);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();


            }
        });

        handle_exit_prompt();

    }

    private void setFeatureText(String text) {
        feature_text.setText(text);
        feature_text.setVisibility(View.VISIBLE);
    }


    private void handle_exit_prompt() {
        int rate_nonRepeat, exit_nonRepeat, full_nonRepeat, removeads_nonRepeat;

        if (Slave.EXIT_NON_REPEAT_COUNT != null && Slave.EXIT_NON_REPEAT_COUNT.size() > 0) {
            for (int i = 0; i < Slave.EXIT_NON_REPEAT_COUNT.size(); i++) {
                rate_nonRepeat = getStringtoInt(Slave.EXIT_NON_REPEAT_COUNT.get(i).rate);
                exit_nonRepeat = getStringtoInt(Slave.EXIT_NON_REPEAT_COUNT.get(i).exit);
                full_nonRepeat = getStringtoInt(Slave.EXIT_NON_REPEAT_COUNT.get(i).full);
                removeads_nonRepeat = getStringtoInt(Slave.EXIT_NON_REPEAT_COUNT.get(i).removeads);

                if (DataHubConstant.APP_LAUNCH_COUNT == rate_nonRepeat) {
                    PrintLog.print("handle exit inside 1 rate");
                    setFeatureText("Rate App");

                    return;
                } else if (DataHubConstant.APP_LAUNCH_COUNT == exit_nonRepeat) {
                    PrintLog.print("handle exit inside 2 cp exit");
                    setFeatureText("More App");
                    return;
                } else if (DataHubConstant.APP_LAUNCH_COUNT == full_nonRepeat) {
                    PrintLog.print("handle exit inside 3 fullads");

                    return;
                } else if (DataHubConstant.APP_LAUNCH_COUNT == removeads_nonRepeat) {
                    PrintLog.print("handle exit inside 4 removeads");
                    setFeatureText("Remove Ads");
                    return;
                }
            }
        }
        PrintLog.print("handle exit repeat check" + " " + DataHubConstant.APP_LAUNCH_COUNT + " " + Slave.EXIT_REPEAT_FULL_ADS);
        if (Slave.EXIT_REPEAT_FULL_ADS != null && !Slave.EXIT_REPEAT_FULL_ADS.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % getStringtoInt(Slave.EXIT_REPEAT_FULL_ADS) == 0) {
            PrintLog.print("handle exit inside 13 fullads" + " " + Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT);

        } else if (Slave.EXIT_REPEAT_EXIT != null && !Slave.EXIT_REPEAT_EXIT.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % getStringtoInt(Slave.EXIT_REPEAT_EXIT) == 0) {
            PrintLog.print("handle exit inside 12 cp exit");
            setFeatureText("More App");
        } else if (Slave.EXIT_REPEAT_RATE != null && !Slave.EXIT_REPEAT_RATE.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % getStringtoInt(Slave.EXIT_REPEAT_RATE) == 0) {
            PrintLog.print("handle exit inside 11 rate");
            setFeatureText("Rate App");
        } else if (Slave.EXIT_REPEAT_REMOVEADS != null && !Slave.EXIT_REPEAT_REMOVEADS.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % getStringtoInt(Slave.EXIT_REPEAT_REMOVEADS) == 0) {
            PrintLog.print("handle exit inside 14 removeads");
            setFeatureText("Remove Ads");
        }
    }


}

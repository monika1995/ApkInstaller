package app.campaign.utils;//package app.campaign.utils;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//import app.campaign.FullAdsUtil;
//import app.pnd.adshandler.R;
//
//
///**
// * Created by hp on 8/10/2017.
// */
//public class FullAdsInfoActivity extends Activity {
//
//    private TextView tv1, tv2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.adinfocampaign);
//
//        tv1 = (TextView) findViewById(R.id.tv1);
//        tv2 = (TextView) findViewById(R.id.tv2);
//
//
//        Intent intent = getIntent();
//        String header = intent.getStringExtra(FullAdsUtil.TAG_HEADER);
//        String footer = intent.getStringExtra(FullAdsUtil.TAG_FOOTER);
//
//        tv1.setText(header);
//        tv2.setText(footer);
//
//        findViewById(R.id.complete).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//
//    }
//}

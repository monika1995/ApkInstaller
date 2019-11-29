package app.campaign.utils;//package app.campaign.utils;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.view.Window;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import app.adshandler.AHandler;
//import app.campaign.CampaignHandler;
//import app.pnd.adshandler.R;
//
//
///**
// * Created by hp on 8/1/2017.
// */
//public class AdPromptActivity extends AppCompatActivity {
//
//    private CampaignHandler handler;
//    private AHandler aHandler;
//
//
//    private LinearLayout layoutBottom, layoutTop;
//    //    private Animation anim;
//    //  private CampaignHandler handler;
//    private TextView btn_exit, btn_cancel;
//    private ImageView icon;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.adprompt);
//
//        handler = CampaignHandler.getInstance();
//        aHandler = new AHandler();
//
//        layoutBottom = (LinearLayout) findViewById(R.id.layoutBottom);
//        layoutBottom.addView(aHandler.showNativeLarge(AdPromptActivity.this));
////        anim = AnimationUtils.loadAnimation(AdPromptActivity.this, R.anim.right_to_left);
//
//
////        new Handler().postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                layoutBottom.setAnimation(anim);
////                layoutBottom.addView(aHandler.showNativeLarge(AdPromptActivity.this));
////            }
////        }, 500);
//
//        handleIntent();
//    }
//
//    private void handleIntent() {
//        Intent intent = getIntent();
//
//        if (intent != null && intent.getExtras() != null) {
//            String title = intent.getExtras().getString(CampaignUtils.TAG_HEADER);
//            String subTitle = intent.getExtras().getString(CampaignUtils.TAG_FOOTER);
//            boolean isShowIcon = intent.getExtras().getBoolean(CampaignUtils.TAG_IS_ICON);
//
//            TextView tv_subTitle = (TextView) findViewById(R.id.tv_subTitle);
//            TextView tv_title = (TextView) findViewById(R.id.tv_title);
//            ImageView iv_icon = (ImageView) findViewById(R.id.iv_icon);
//
//            if (title.equalsIgnoreCase("") || title.equalsIgnoreCase("na")) {
//                tv_title.setVisibility(View.GONE);
//            } else {
//                tv_title.setText(title);
//            }
//
//            if (subTitle.equalsIgnoreCase("") || subTitle.equalsIgnoreCase("na")) {
//                tv_subTitle.setVisibility(View.GONE);
//            } else {
//                tv_subTitle.setText(subTitle);
//            }
//            if (isShowIcon)
//                iv_icon.setVisibility(View.VISIBLE);
//            else
//                iv_icon.setVisibility(View.INVISIBLE);
//
//        }
//    }
//}

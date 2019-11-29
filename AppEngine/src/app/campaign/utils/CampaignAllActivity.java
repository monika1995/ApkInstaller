package app.campaign.utils;//package app.campaign.utils;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Point;
//import android.graphics.drawable.GradientDrawable;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.Toolbar;
//import android.view.Display;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//import app.adshandler.AHandler;
//import app.campaign.CampaignConstant;
//import app.campaign.CampaignHandler;
//import app.campaign.response.Redirection;
//import app.pnd.adshandler.R;
//
//
///**
// * Created by hp on 7/20/2017.
// */
//public class CampaignAllActivity extends AppCompatActivity {
//
//    private CampaignHandler handler;
//    private CampaignConstant constant;
//    private List<Redirection> campaignList;
//    private LinearLayout campaignContainer;
//    private LayoutInflater inflater;
//    private Activity mContext;
//    private AHandler aHandler;
//    private Toolbar mToolbar;
//    private TextView tv_subTitle, tv_title;
//    private ImageView iv_icon;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.campaign_all_activity);
//
//        handleToolbar();
//        initScreenConfig();
//        this.aHandler = new AHandler();
//        this.mContext = CampaignAllActivity.this;
//        constant = new CampaignConstant(mContext);
//        inflater = LayoutInflater.from(this);
//        campaignContainer = (LinearLayout) findViewById(R.id.campaignContainer);
//        handler = CampaignHandler.getInstance();
//
//        handleIntent();
//
//        if (handler.loadAllCampaignList() != null) {
//            campaignList = handler.loadAllCampaignList();
//
//
//            for (Redirection r : campaignList) {
//                System.out.println("147 prio all prio" + " " + r.priority + " " + r.type + " " + r.addId);
//            }
//
//
//            for (int pos = 0; pos < campaignList.size(); pos++) {
//                View view;
//                if (campaignList.get(pos).is_large) {
//                    if (campaignList.get(pos).type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_ADS)) {
//                        view = getLargeAdsLayout(campaignList.get(pos));
//                    } else {
//                        view = getLargeCampaign(campaignList.get(pos));
//                    }
//                } else {
//                    if (campaignList.get(pos).type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_ADS)) {
//                        view = getSmallAdsLayout(campaignList.get(pos));
//                    } else {
//                        view = getSmallCampaigns(campaignList.get(pos));
//                    }
//                }
//                if (view != null && campaignContainer != null) {
//                    campaignContainer.addView(view);
//
//                    if (campaignList.get(pos).type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_DEEPLINK) ||
//                            campaignList.get(pos).type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_URL)) {
//
//                        int gap = mContext.getResources().getDimensionPixelSize(R.dimen.campaign_padding);
//
//                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
//                        params.setMargins(gap, gap, gap, gap);
//                    }
//                }
//            }
//        }
//    }
//
//    private View getLargeAdsLayout(Redirection r) {
//        System.out.println("0621 floating" + " " + r.addId);
//        if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_BANNER))
//            return aHandler.getBannerHeader(mContext);
//        else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_LARGE))
//            return aHandler.showNativeLarge(mContext);
//
//        else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_MEDIUM))
//            return aHandler.showNativeMedium(mContext);
//        else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_SMALL))
//            return aHandler.getBannerHeader(mContext);
//
//        else
//            return null;
//    }
//
//    private View getSmallAdsLayout(Redirection r) {
//
//        if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_BANNER))
//            return aHandler.getBannerHeader(mContext);
//        else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_LARGE))
//            return aHandler.showNativeLarge(mContext);
//
//        else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_MEDIUM))
//            return aHandler.showNativeMedium(mContext);
//        else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_SMALL))
//            return aHandler.showNativeMedium(mContext);
//        else
//            return null;
//    }
//
//    private View getSmallCampaigns(final Redirection r) {
//
//        if (r.format.equalsIgnoreCase(CampaignConstant.FORMAT_TEXT)) {
//
//            View view = inflater.inflate(R.layout.campaign_small_text_row, null);
//
//            CardView root = (CardView) view.findViewById(R.id.smallRoot);
//
//            if (r.layout_bgcolor != null && !r.layout_bgcolor.equalsIgnoreCase(""))
//                root.setCardBackgroundColor(Color.parseColor(r.layout_bgcolor));
//
//            TextView header = (TextView) view.findViewById(R.id.header);
//            TextView footer = (TextView) view.findViewById(R.id.footer);
//            ImageView icon = (ImageView) view.findViewById(R.id.thumb);
//            Button btn = (Button) view.findViewById(R.id.btn_campaign);
//            GradientDrawable gD = new GradientDrawable();
//            gD.setCornerRadius(8);
//
//            if (r.header_text != null && !r.header_text.equalsIgnoreCase(""))
//                header.setText(r.header_text);
//            if (r.header_text_color != null && !r.header_text_color.equalsIgnoreCase(""))
//                header.setTextColor(Color.parseColor(r.header_text_color));
//
//            if (r.footer_text != null && !r.footer_text.equalsIgnoreCase(""))
//                footer.setText(r.footer_text);
//            if (r.footer_text_color != null && !r.footer_text_color.equalsIgnoreCase(""))
//                footer.setTextColor(Color.parseColor(r.footer_text_color));
//
//            if (r.imageUrl != null && !r.imageUrl.equalsIgnoreCase(""))
//                Picasso.with(mContext).load(r.imageUrl).into(icon);
//
//            if (r.button_text != null && !r.button_text.equalsIgnoreCase(""))
//                btn.setText(r.button_text);
//
//            if (r.button_color != null && !r.button_color.equalsIgnoreCase(""))
//                gD.setColor(Color.parseColor(r.button_color));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                btn.setBackground(gD);
//            }
//            if (r.button_text_color != null && !r.button_text_color.equalsIgnoreCase(""))
//                btn.setTextColor(Color.parseColor(r.button_text_color));
//
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_URL)) {
//                        if (r.clickurl != null)
//                            constant.openURL(mContext, r.clickurl);
//                    } else if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_DEEPLINK)) {
//                        if (r.pageId != null)
//                            constant.openDeepLink(mContext, r.pageId);
//                    }
//                }
//            });
//
//
//            root.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_URL)) {
//                        if (r.clickurl != null)
//                            constant.openURL(mContext, r.clickurl);
//                    } else if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_DEEPLINK)) {
//                        if (r.pageId != null)
//                            constant.openDeepLink(mContext, r.pageId);
//                    }
//                }
//            });
//
//            return view;
//        } else {
//            View view = inflater.inflate(R.layout.campaign_small_image_row, null);
//
//            ImageView image = (ImageView) view.findViewById(R.id.iv_small_image);
//            RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parentLayout);
//            System.out.println("please print prio" + " " + r.priority);
//
//
//            if (r.imageUrl != null && !r.imageUrl.equalsIgnoreCase(""))
//                Picasso.with(mContext).load(r.imageUrl).into(image);
//
//
//            parent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_URL)) {
//                        constant.openURL(mContext, r.clickurl);
//                    } else if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_DEEPLINK)) {
//                        constant.openDeepLink(mContext, r.pageId);
//                    }
//                }
//            });
//
//            return view;
//        }
//    }
//
//    private View getLargeCampaign(final Redirection r) {
//        View view = inflater.inflate(R.layout.campaign_large_row, null);
//
//        ImageView image = (ImageView) view.findViewById(R.id.largeImageCampaign);
//
//        if (r.imageUrl != null && !r.imageUrl.equalsIgnoreCase(""))
//            loadImage(r.imageUrl, image, screenWidth - getDimenPixel(R.dimen.ad_one_padding)
//                            - getDimenPixel(R.dimen.ad_one_padding),
//                    getDimenPixel(R.dimen.ad_one_banner_height));
//
//
////            Picasso.with(mContext).load(r.imageUrl).into(image);
//
//
//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_URL)) {
//                    constant.openURL(mContext, r.clickurl);
//                } else if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_DEEPLINK)) {
//                    constant.openDeepLink(mContext, r.pageId);
//                }
//            }
//        });
//
//        return view;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    private void handleToolbar() {
//        this.mToolbar = (Toolbar) findViewById(R.id.campaignToolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
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
//            tv_subTitle = (TextView) findViewById(R.id.tv_subTitle);
//            tv_title = (TextView) findViewById(R.id.tv_title);
//            iv_icon = (ImageView) findViewById(R.id.iv_icon);
//
//            tv_title.setText(title);
//            tv_subTitle.setText(subTitle);
//
//            if (isShowIcon)
//                iv_icon.setVisibility(View.VISIBLE);
//            else
//                iv_icon.setVisibility(View.INVISIBLE);
//
//        }
//    }
//
//    //
//    private void loadImage(String url, ImageView mImageView, int mWidth, int mHeight) {
//        Picasso.with(mContext)
//                .load(url)
////                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                .resize(mWidth, mHeight)
//                .placeholder(R.drawable.transparent)
//                .into(mImageView);
//    }
//
//    private int screenWidth;
//
//    private void initScreenConfig() {
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        screenWidth = size.x;
//    }
//
//    private int getDimenPixel(int resId) {
//        return (int) mContext.getResources().getDimension(resId);
//    }
//}

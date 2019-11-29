package app.campaign;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.cardview.widget.CardView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.PrintLog;
import app.adshandler.AHandler;
import app.campaign.request.CampaignDataRequest;
import app.campaign.response.AdsIcon;
import app.campaign.response.CampaignDataResponse;
import app.campaign.response.CampaignResponse;
import app.campaign.response.NotificationTime;
import app.campaign.response.PageConfig;
import app.campaign.response.Redirection;
import app.ecrypt.MCrypt;
import app.pnd.adshandler.R;
import app.server.v2.DataHubPreference;

/**
 * Created by hp on 7/20/2017.
 */
public final class CampaignHandler {
    private static CampaignHandler ourInstance = new CampaignHandler();

    public static synchronized CampaignHandler getInstance() {
        return ourInstance;
    }


    private ICampaignLoad iCampaignLoad;
    //    private List<Redirection> campaignResponse;
    private CampaignResponse campaignResponse;
    private CampaignConstant constant;
    private ArrayList<Redirection> largeList, smallList;
    private ArrayList<AdsIcon> iconList;
    private ArrayList<PageConfig> pageConfig;
    private int mScreenWidth;
    //    private AHandler aHandler;
    private LayoutInflater inflater;
    private Activity mContext;
    private DataHubPreference mPreferences;
    private NotificationTime notificationTime;

    private CampaignHandler() {
    }


    public void initCampaign(Context mContext, ICampaignLoad l) {
        this.constant = new CampaignConstant(mContext);
        this.inflater = LayoutInflater.from(mContext);
        this.mContext = (Activity) mContext;
        this.notificationTime = new NotificationTime();
        //this.aHandler = AHandler.getInstance();
        this.largeList = new ArrayList<>();
        this.iconList = new ArrayList<>();
        this.smallList = new ArrayList<>();
        this.pageConfig = new ArrayList<>();
        Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.mScreenWidth = size.x;
        int mScreenHeight = size.y;
        this.mPreferences = new DataHubPreference(this.mContext);

        this.iCampaignLoad = l;

        getDataTask();
    }

    /**
     * checks the string value of isStatic in campaign response.
     *
     * @return "true" to show bannerRectangle if isStatic is true from server &
     * "false" to show sliding large banners if isStatic is false from server.
     */
    public boolean shouldShowBannerLarge() {
        try {
            if (campaignResponse != null && campaignResponse.isStatic != null) {
                return campaignResponse.isStatic.equalsIgnoreCase("true");
            }
            return true;
        } catch (Exception e) {
            return true;
        }

    }

    public ArrayList<PageConfig> loadPageConfigurations() {
        if (pageConfig != null && pageConfig.size() > 0) {
            return this.pageConfig;
        } else {
            return null;
        }
    }

    public List<Redirection> loadAllCampaignList() {
        if (campaignResponse != null && this.campaignResponse.redirection != null) {
            return this.campaignResponse.redirection;
        } else {
            return null;
        }
    }

    public ArrayList<Redirection> loadLargeCampaignList() {
        if (largeList != null && this.largeList.size() > 0) {
            return this.largeList;
        } else {
            return null;
        }
    }

    //for dashboard
    public View getLargeAdsViewsForDashBoard(Activity a, int pos) {
        if (largeList.get(pos).type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_ADS)) {
            return getAdsLayout(a, largeList.get(pos));
        } else {
            return getLargeCampaignForDashBoard(a, largeList.get(pos));
        }
    }

    private View getLargeCampaignForDashBoard(final Activity mContext, final Redirection r) {
        if (inflater == null) {
            inflater = LayoutInflater.from(mContext);
        }

        View view = inflater.inflate(R.layout.campaign_large_dashboard, null);

        ImageView image = view.findViewById(R.id.largeImageCampaign);

        if (r.imageUrl != null && !r.imageUrl.equalsIgnoreCase(""))
//            System.out.println("loading imageWidth" + " " +
//                    image.getWidth() + " " + mScreenHeight + " " + getStatusBarHeight(mContext)
//                    + " " +
//                    getToolBarHeight(mContext) + " " + (mScreenHeight - getToolBarHeight(mContext) - getStatusBarHeight(mContext)));
//              int h = mScreenHeight - getStatusBarHeight(mContext) - getToolBarHeight(mContext);
//            System.out.println("height int " + " " + h);
        {
            loadImage(mContext, r.imageUrl, image);
        }
//            loadImage(r.imageUrl, image, mScreenWidth - getDimenPixel(R.dimen.ad_one_padding)
//                            - getDimenPixel(R.dimen.ad_one_padding),
//                    getDimenPixel(R.dimen.ad_one_banner_height));

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_URL)) {
                    constant.openURL(mContext, r.clickurl);
                } else if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_DEEPLINK)) {
                    constant.openDeepLink(mContext, r.pageId);
                }
            }
        });

        return view;
    }


    private View getAdsLayout(final Activity mContext, Redirection r) {
        System.out.println("0621 dashboard" + " " + r.addId);
        if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_BANNER)) {
            return AHandler.getInstance().getBannerHeader(mContext);
        } else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_LARGE)) {
            return AHandler.getInstance().getNativeLarge(mContext);
        } else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_MEDIUM)) {
            return AHandler.getInstance().getNativeMedium(mContext);
        } else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_SMALL)) {
            return AHandler.getInstance().getNativeMedium(mContext);
        } else {
            return null;
        }
    }


    private void getDataTask() {
        CampaignDataRequest dataRequest = new CampaignDataRequest();

        CampaignApiController controller = new CampaignApiController(this.mContext, new ICampaignResponseCallback() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                PrintLog.print("response campaign OK" + " " + response.toString());
                parseEncrypterCampaigns(response.toString());
            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                PrintLog.print("response campaign ERROR" + " " + errormsg);
                parseEncrypterCampaigns(constant.readFromAssets("value.txt"));
            }
        }, CampaignApiController.CAMPAIGN_SERVICE_CODE);
        controller.getCampaignRequest(dataRequest);

    }

    private void parseEncrypterCampaigns(String encrypt) {
        CampaignDataResponse dataResponse;


        if (encrypt != null) {
            Gson gson = new Gson();
            MCrypt mCrypt = new MCrypt();

            dataResponse = gson.fromJson(encrypt, CampaignDataResponse.class);

            try {
                String dResponse = new String(mCrypt.decrypt(dataResponse.data));
                PrintLog.print("parsing campaign data decrypt value" + " " + dResponse);

                parseCampagin(dResponse, encrypt);

            } catch (Exception e) {
                PrintLog.print("exception campaign response" + " " + e);
                if (!mPreferences.getCampaignJSON().equalsIgnoreCase("NA")) {
                    parseEncrypterCampaigns(mPreferences.getCampaignJSON());
                } else {
                    parseEncrypterCampaigns(constant.readFromAssets("value.txt"));
                }
            }

        }
    }

    private void parseCampagin(String response, String encrypts) {
        Gson gson = new Gson();
        try {
            if (response != null) {

                campaignResponse = gson.fromJson(response, CampaignResponse.class);

                if (campaignResponse != null && campaignResponse.message.equalsIgnoreCase("success")) {
                    if (campaignResponse.redirection != null
                            && campaignResponse.redirection.size() > 0) {
                        Collections.sort(campaignResponse.redirection, new PriorityComparator());


                        for (Redirection redirection : campaignResponse.redirection) {
                            if (redirection.is_large) {
                                if (largeList != null) {
                                    largeList.add(redirection);
                                }

                            } else if (!redirection.is_large) {
                                if (smallList != null) {
                                    smallList.add(redirection);
                                }
                            }
                        }
                        if (iCampaignLoad != null) {
                            iCampaignLoad.onLargeCampaignLoad(largeList);
                        }
                        if (iCampaignLoad != null) {
                            iCampaignLoad.onSmallCampaignLoad(smallList);
                        }
                    }

                    if (campaignResponse.icons != null && campaignResponse.icons.size() > 0) {
                        Collections.sort(campaignResponse.icons, new IconPriorityComparator());
                        for (AdsIcon i : campaignResponse.icons) {
                            if (iconList != null) {
                                iconList.add(i);
                            }
                        }

                        if (iCampaignLoad != null) {
                            this.iCampaignLoad.onCPIconLoad(iconList);
                        }
                    }
                    if (campaignResponse.pageconfig != null && campaignResponse.pageconfig.size() > 0) {
                        for (PageConfig c : campaignResponse.pageconfig) {
                            if (pageConfig != null) {
                                pageConfig.add(c);
                            }
                        }
                    }

                    if (campaignResponse.features != null) {
                        try {
                            notificationTime = campaignResponse.features;
//                            pref.setBatteryPeriod(Integer.parseInt(notificationTime.KEY_BATTERY));
//                            pref.setRAMPeriod(Integer.parseInt(notificationTime.KEY_RAM));
//                            pref.setTempPeriod(Integer.parseInt(notificationTime.KEY_TEMP));
//                            pref.setImageDupPeriod(Integer.parseInt(notificationTime.KEY_IMAGEDUPLICATE));
//                            pref.setStoragePeriod(Integer.parseInt(notificationTime.KEY_STORAGE));
//                            pref.setCachePeriod(Integer.parseInt(notificationTime.KEY_CACHE));
//                            pref.setJunkPeriod(Integer.parseInt(notificationTime.KEY_JUNK));
                        } catch (Exception e) {

//                            pref.setBatteryPeriod((int) NotificationValue.PERIOD_BATTERY);
//                            pref.setRAMPeriod((int) NotificationValue.PERIOD_RAM);
//                            pref.setTempPeriod((int) NotificationValue.PERIOD_TEMP);
//                            pref.setImageDupPeriod((int) NotificationValue.PERIOD_IMAGE_DUP);
//                            pref.setStoragePeriod((int) NotificationValue.PERIOD_STORAGE);
//                            pref.setCachePeriod((int) NotificationValue.PERIOD_CACHE);
//                            pref.setJunkPeriod((int) NotificationValue.PERIOD_JUNK);
                        }
                    }
                    mPreferences.setCampaginJSON(encrypts);
                } else {
                    if (!mPreferences.getCampaignJSON().equalsIgnoreCase("NA")) {
                        parseEncrypterCampaigns(mPreferences.getCampaignJSON());
                    } else {
                        parseEncrypterCampaigns(constant.readFromAssets("value.txt"));
                    }
                }
            } else {
                if (!mPreferences.getCampaignJSON().equalsIgnoreCase("NA")) {
                    parseEncrypterCampaigns(mPreferences.getCampaignJSON());
                } else {
                    parseEncrypterCampaigns(constant.readFromAssets("value.txt"));
                }
            }
        } catch (Exception e) {
            if (!mPreferences.getCampaignJSON().equalsIgnoreCase("NA")) {
                parseEncrypterCampaigns(mPreferences.getCampaignJSON());
            } else {
                parseEncrypterCampaigns(constant.readFromAssets("value.txt"));
            }
        }
    }


    public ArrayList<AdsIcon> loadIconAdsList() {
        return iconList;
    }

    private class PriorityComparator implements Comparator<Redirection> {
        @Override
        public int compare(Redirection o1, Redirection o2) {
            return Integer.valueOf(o1.priority).compareTo(Integer.valueOf(o2.priority));
        }
    }

    private class IconPriorityComparator implements Comparator<AdsIcon> {
        @Override
        public int compare(AdsIcon o1, AdsIcon o2) {
            return Integer.valueOf(o1.priority).compareTo(Integer.valueOf(o2.priority));
        }

    }

    private View getLargeAdsLayout(Redirection r) {
        PrintLog.print("0621 floating" + " " + r.addId);
        if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_BANNER)) {
            return AHandler.getInstance().getBannerHeader(mContext);
        } else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_LARGE)) {
            return AHandler.getInstance().getNativeLarge(mContext);
        } else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_MEDIUM)) {
            return AHandler.getInstance().getNativeMedium(mContext);
        } else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_SMALL)) {
            return AHandler.getInstance().getBannerHeader(mContext);
        } else {
            return null;
        }
    }

    private View getLargeCampaign(final Redirection r) {
        View view = inflater.inflate(R.layout.campaign_large_row, null);

        ImageView image = view.findViewById(R.id.largeImageCampaign);

        if (r.imageUrl != null && !r.imageUrl.equalsIgnoreCase("")) {
            loadImage(r.imageUrl, image, mScreenWidth - getDimenPixel(R.dimen.ad_one_padding)
                            - getDimenPixel(R.dimen.ad_one_padding),
                    getDimenPixel(R.dimen.ad_one_banner_height));
        }


//            Picasso.with(mContext).load(r.imageUrl).into(image);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_URL)) {
                    constant.openURL(mContext, r.clickurl);
                } else if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_DEEPLINK)) {
                    constant.openDeepLink(mContext, r.pageId);
                }
            }
        });

        return view;
    }

    private View getSmallAdsLayout(Redirection r) {

        if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_BANNER)) {
            return AHandler.getInstance().getBannerHeader(mContext);
        } else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_LARGE)) {
            return AHandler.getInstance().getNativeLarge(mContext);
        } else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_MEDIUM)) {
            return AHandler.getInstance().getNativeMedium(mContext);
        } else if (r.addId.equalsIgnoreCase(CampaignConstant.ADS_NATIVE_SMALL)) {
            return AHandler.getInstance().getBannerHeader(mContext);
        } else {
            return null;
        }
    }

    private View getSmallCampaigns(final Redirection r) {

        if (r.format.equalsIgnoreCase(CampaignConstant.FORMAT_TEXT)) {

            View view = inflater.inflate(R.layout.campaign_small_text_row, null);

            CardView root = view.findViewById(R.id.smallRoot);

            if (r.layout_bgcolor != null && !r.layout_bgcolor.equalsIgnoreCase("")) {
                root.setCardBackgroundColor(Color.parseColor(r.layout_bgcolor));
            }

            TextView header = view.findViewById(R.id.header);
            TextView footer = view.findViewById(R.id.footer);
            ImageView icon = view.findViewById(R.id.thumb);
            Button btn = view.findViewById(R.id.btn_campaign);
            GradientDrawable gD = new GradientDrawable();
            gD.setCornerRadius(8);

            if (r.header_text != null && !r.header_text.equalsIgnoreCase("")) {
                header.setText(r.header_text);
            }
            if (r.header_text_color != null && !r.header_text_color.equalsIgnoreCase("")) {
                header.setTextColor(Color.parseColor(r.header_text_color));
            }

            if (r.footer_text != null && !r.footer_text.equalsIgnoreCase("")) {
                footer.setText(r.footer_text);
            }
            if (r.footer_text_color != null && !r.footer_text_color.equalsIgnoreCase("")) {
                footer.setTextColor(Color.parseColor(r.footer_text_color));
            }

            if (r.imageUrl != null && !r.imageUrl.equalsIgnoreCase("")) {
                Picasso.get().load(r.imageUrl).into(icon);
            }

            if (r.button_text != null && !r.button_text.equalsIgnoreCase("")) {
                btn.setText(r.button_text);
            }

            if (r.button_color != null && !r.button_color.equalsIgnoreCase("")) {
                gD.setColor(Color.parseColor(r.button_color));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btn.setBackground(gD);
            }
            if (r.button_text_color != null && !r.button_text_color.equalsIgnoreCase("")) {
                btn.setTextColor(Color.parseColor(r.button_text_color));
            }

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_URL)) {
                        if (r.clickurl != null) {
                            constant.openURL(mContext, r.clickurl);
                        }
                    } else if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_DEEPLINK)) {
                        if (r.pageId != null) {
                            constant.openDeepLink(mContext, r.pageId);
                        }
                    }
                }
            });


            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_URL)) {
                        if (r.clickurl != null) {
                            constant.openURL(mContext, r.clickurl);
                        }
                    } else if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_DEEPLINK)) {
                        if (r.pageId != null) {
                            constant.openDeepLink(mContext, r.pageId);
                        }
                    }
                }
            });

            return view;
        } else {
            View view = inflater.inflate(R.layout.campaign_small_image_row, null);

            ImageView image = view.findViewById(R.id.iv_small_image);
            RelativeLayout parent = view.findViewById(R.id.parentLayout);
            PrintLog.print("please print prio" + " " + r.priority);


            if (r.imageUrl != null && !r.imageUrl.equalsIgnoreCase("")) {
                Picasso.get().load(r.imageUrl).into(image);
            }


            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_URL)) {
                        constant.openURL(mContext, r.clickurl);
                    } else if (r.type.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_DEEPLINK)) {
                        constant.openDeepLink(mContext, r.pageId);
                    }
                }
            });

            return view;
        }
    }

    private void loadImage(String url, ImageView mImageView, int mWidth, int mHeight) {
        Picasso.get()
                .load(url)
//                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .resize(mWidth, mHeight)
                .placeholder(R.drawable.blank)
                .into(mImageView);
    }


    private void loadImage(Context context, String url, ImageView mImageView) {
        Picasso.get()
                .load(url)
                .fit()
                .centerCrop()
                .into(mImageView);
    }

    private int getDimenPixel(int resId) {
        return (int) mContext.getResources().getDimension(resId);
    }
}

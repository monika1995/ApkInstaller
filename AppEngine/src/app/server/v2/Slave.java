package app.server.v2;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import app.inapp.BillingPreference;
import app.server.v4.AdsProviders;

/**
 * Created by hp on 9/20/2017.
 */
public class Slave {

    public static boolean IS_PRO = false, IS_WEEKLY = false, IS_MONTHLY = false, IS_HALFYEARLY = false, IS_YEARLY = false;

    public static final String NATIVE_TYPE_SMALL = "native_small";
    public static final String NATIVE_TYPE_MEDIUM = "native_medium";
    public static final String NATIVE_TYPE_LARGE = "native_large";

    public static final String BANNER_TYPE_LARGE = "banner_large";
    public static final String BANNER_TYPE_RECTANGLE = "banner_rectangle";
    public static final String BANNER_TYPE_HEADER = "top_banner";

    public static final String IS_NORMAL_UPDATE = "2";
    public static final String IS_FORCE_UPDATE = "1";

    public static final String CP_YES = "yes";
    public static final String CP_NO = "NO";


    public static String ADMOB_NATIVE_MEDIUM_ID_STATIC = "";
    public static String ADMOB_NATIVE_LARGE_ID_STATIC = "";
    public static String ADMOB_BANNER_ID_STATIC = "";
    public static String ADMOB_FULL_ID_STATIC = "";
    public static String ADMOB_BANNER_ID_LARGE_STATIC = "";
    public static String ADMOB_BANNER_ID_RECTANGLE_STATIC = "";


    public static final String Provider_Admob_Native_Small = "Admob_Native_Small";
    public static final String Provider_Admob_Native_Medium = "Admob_Native_Medium";
    public static final String Provider_Admob_Native_Large = "Admob_Native_Large";
    public static final String Provider_Admob_Banner = "Admob_Banner";
    public static final String Provider_Admob_Banner_Rectangle = "Admob_Banner_Rectangle";
    public static final String Provider_Admob_Banner_Large = "Admob_Banner_Large";
    public static final String Provider_Admob_FullAds = "Admob_FullAds";

    public static final String Provider_Inhouse_Banner = "Inhouse_Banner";
    public static final String Provider_Inhouse_Banner_Large = "Inhouse_Banner_Large";
    public static final String Provider_Inhouse_Banner_Rect = "Inhouse_Banner_Rectangle";
    public static final String Provider_Inhouse_FullAds = "Inhouse_FullAds";
    public static final String Provider_Inhouse_Icon = "Inhouse_Icon";
    public static final String Provider_Inhouse_Medium = "Inhouse_Medium";
    public static final String Provider_Inhouse_Icon_With_Text_Small = "Inhouse_Icon_With_Text_Small";
    public static final String Provider_Inhouse_Icon_With_Text_Medium = "Inhouse_Icon_With_Text_Medium";
    public static final String Provider_Inhouse_Large = "Inhouse_Large";

    public static final String Provider_Facebook_Banner = "Facebook_Banner";
    public static final String Provider_Facebook_Native_Small = "Facebook_Native_Small";
    public static final String Provider_Facebook_Banner_Rect = "Facebook_Banner_Rectangle";
    public static final String Provider_Facebook_Banner_Large = "Facebook_Banner_Large";
    public static final String Provider_Facebook_Native_Medium = "Facebook_Native_Medium";
    public static final String Provider_Facebook_Native_Large = "Facebook_Native_Large";
    public static final String Provider_Facebook_Full_Page_Ads = "Facebook_Full_Page_Ads";

    public static final String Provider_Startapp_Banner = "Startapp_Banner";
    public static final String Provider_Startapp_FullAds = "Startapp_FullAds";
    public static final String Provider_Startapp_Native_Large = "Startapp_Native_Large";
    public static final String Provider_Startapp_Native_Medium = "Startapp_Native_Medium";


    public static final String Provider_Applovin_Banner = "Applovin_Banner";
    public static final String Provider_Applovin_Banner_Large = "Applovin_Banner_Large";
    public static final String Provider_Applovin_Banner_Rectangle = "Applovin_Banner_Rectangle";
    public static final String Provider_Applovin_Native_Medium = "Applovin_Native_Medium";
    public static final String Provider_Applovin_Native_Large = "Applovin_Native_Large";
    public static final String Provider_Applovin_FullAds_Page_Ads = "Applovin_Full_Ads";

    public static final String Provider_Vungle_Banner_Rect = "Vungle_Banner_Rectangle";
    public static final String Provider_Vungle_Full_Page_Ads = "Vungle_Full_Ads";

    public static final String Provider_Unity_Banner = "Unity_Banner";
    public static final String Provider_Unity_Full_Page_Ads = "Unity_Full_Ads";

    public static final String TYPE_FIRST_ADS = "first_ads";
    public static final String TYPE_TOP_BANNER = "top_banner";
    public static final String TYPE_BOTTOM_BANNER = "bottom_banner";
    public static final String TYPE_BANNER_LARGE = "banner_large";
    public static final String TYPE_BANNER_RECTANGLE = "banner_rectangle";
    public static final String TYPE_FULL_ADS = "full_ads";
    public static final String TYPE_LAUNCH_FULL_ADS = "launch_full_ads";
    public static final String TYPE_EXIT_FULL_ADS = "exit_full_ads";
    public static final String TYPE_NATIVE_SMALL = "native_small";
    public static final String TYPE_NATIVE_MEDIUM = "native_medium";
    public static final String TYPE_NATIVE_LARGE = "native_large";
    public static final String TYPE_RATE_APP = "rateapp";
    public static final String TYPE_FEEDBACK = "feedback";
    public static final String TYPE_UPDATES = "updates";
    public static final String TYPE_MORE_APPS = "moreapp";
    public static final String TYPE_ETC = "etc";
    public static final String TYPE_SHARE = "shareapp";
    public static final String TYPE_ADMOB_STATIC = "admob_static";
    public static final String TYPE_ABOUT_DETAILS = "aboutdetails";
    public static final String TYPE_REMOVE_ADS = "removeads";
    public static final String TYPE_EXIT_NON_REPEAT = "exitnonrepeat";
    public static final String TYPE_EXIT_REPEAT = "exitrepeat";
    public static final String TYPE_LAUNCH_NON_REPEAT = "launch_nonrepeat";
    public static final String TYPE_LAUNCH_REPEAT = "launch_repeat";
    public static final String TYPE_INAPP_BILLING = "inapp_billing";

    public static List<AdsProviders> TOP_BANNER_providers = new ArrayList<>();
    //public static String TOP_BANNER_ad_id = "";
    //public static String TOP_BANNER_provider_id = "";
    public static String TOP_BANNER_clicklink = "";
    public static String TOP_BANNER_start_date = "";
    public static String TOP_BANNER_nevigation = "";
    public static String TOP_BANNER_call_native = "";
    public static String TOP_BANNER_rateapptext = "";
    public static String TOP_BANNER_rateurl = "";
    public static String TOP_BANNER_email = "";
    public static String TOP_BANNER_updateTYPE = "";
    public static String TOP_BANNER_appurl = "";
    public static String TOP_BANNER_prompttext = "";
    public static String TOP_BANNER_version = "";
    public static String TOP_BANNER_moreurl = "";
    public static String TOP_BANNER_src = "";

    public static List<AdsProviders> BOTTOM_BANNER_providers = new ArrayList<>();
    //public static String BOTTOM_BANNER_ad_id = "";
    //public static String BOTTOM_BANNER_provider_id = "";
    public static String BOTTOM_BANNER_clicklink = "";
    public static String BOTTOM_BANNER_start_date = "";
    public static String BOTTOM_BANNER_nevigation = "";
    public static String BOTTOM_BANNER_call_native = "";
    public static String BOTTOM_BANNER_rateapptext = "";
    public static String BOTTOM_BANNER_rateurl = "";
    public static String BOTTOM_BANNER_email = "";
    public static String BOTTOM_BANNER_updateTYPE = "";
    public static String BOTTOM_BANNER_appurl = "";
    public static String BOTTOM_BANNER_prompttext = "";
    public static String BOTTOM_BANNER_version = "";
    public static String BOTTOM_BANNER_moreurl = "";
    public static String BOTTOM_BANNER_src = "";

    public static List<AdsProviders> LARGE_BANNER_providers = new ArrayList<>();
    //public static String LARGE_BANNER_ad_id = "";
    //public static String LARGE_BANNER_provider_id = "";
    public static String LARGE_BANNER_clicklink = "";
    public static String LARGE_BANNER_start_date = "";
    public static String LARGE_BANNER_nevigation = "";
    public static String LARGE_BANNER_call_native = "";
    public static String LARGE_BANNER_rateapptext = "";
    public static String LARGE_BANNER_rateurl = "";
    public static String LARGE_BANNER_email = "";
    public static String LARGE_BANNER_updateTYPE = "";
    public static String LARGE_BANNER_appurl = "";
    public static String LARGE_BANNER_prompttext = "";
    public static String LARGE_BANNER_version = "";
    public static String LARGE_BANNER_moreurl = "";
    public static String LARGE_BANNER_src = "";

    public static List<AdsProviders> RECTANGLE_BANNER_providers = new ArrayList<>();
    //public static String RECTANGLE_BANNER_ad_id = "";
    //public static String RECTANGLE_BANNER_provider_id = "";
    public static String RECTANGLE_BANNER_clicklink = "";
    public static String RECTANGLE_BANNER_start_date = "";
    public static String RECTANGLE_BANNER_nevigation = "";
    public static String RECTANGLE_BANNER_call_native = "";
    public static String RECTANGLE_BANNER_rateapptext = "";
    public static String RECTANGLE_BANNER_rateurl = "";
    public static String RECTANGLE_BANNER_email = "";
    public static String RECTANGLE_BANNER_updateTYPE = "";
    public static String RECTANGLE_BANNER_appurl = "";
    public static String RECTANGLE_BANNER_prompttext = "";
    public static String RECTANGLE_BANNER_version = "";
    public static String RECTANGLE_BANNER_moreurl = "";
    public static String RECTANGLE_BANNER_src = "";

    public static List<AdsProviders> FULL_ADS_providers = new ArrayList<>();
    //public static String FULL_ADS_ad_id = "";
    //public static String FULL_ADS_provider_id = "";
    public static String FULL_ADS_clicklink = "";
    public static String FULL_ADS_start_date = "";
    public static String FULL_ADS_nevigation = "3";
    public static String FULL_ADS_call_native = "";
    public static String FULL_ADS_rateapptext = "";
    public static String FULL_ADS_rateurl = "";
    public static String FULL_ADS_email = "";
    public static String FULL_ADS_updateTYPE = "";
    public static String FULL_ADS_appurl = "";
    public static String FULL_ADS_prompttext = "";
    public static String FULL_ADS_version = "";
    public static String FULL_ADS_moreurl = "";
    public static String FULL_ADS_src = "";

    public static List<AdsProviders> LAUNCH_FULL_ADS_providers = new ArrayList<>();
    //public static String LAUNCH_FULL_ADS_ad_id = "";
    //public static String LAUNCH_FULL_ADS_provider_id = "";
    public static String LAUNCH_FULL_ADS_clicklink = "";
    public static String LAUNCH_FULL_ADS_start_date = "";
    public static String LAUNCH_FULL_ADS_nevigation = "";
    public static String LAUNCH_FULL_ADS_call_native = "";
    public static String LAUNCH_FULL_ADS_rateapptext = "";
    public static String LAUNCH_FULL_ADS_rateurl = "";
    public static String LAUNCH_FULL_ADS_email = "";
    public static String LAUNCH_FULL_ADS_updateTYPE = "";
    public static String LAUNCH_FULL_ADS_appurl = "";
    public static String LAUNCH_FULL_ADS_prompttext = "";
    public static String LAUNCH_FULL_ADS_version = "";
    public static String LAUNCH_FULL_ADS_moreurl = "";
    public static String LAUNCH_FULL_ADS_src = "";

    public static List<AdsProviders> EXIT_FULL_ADS_providers = new ArrayList<>();
    //public static String EXIT_FULL_ADS_ad_id = "";
    //public static String EXIT_FULL_ADS_provider_id = "";
    public static String EXIT_FULL_ADS_clicklink = "";
    public static String EXIT_FULL_ADS_start_date = "";
    public static String EXIT_FULL_ADS_nevigation = "";
    public static String EXIT_FULL_ADS_call_native = "";
    public static String EXIT_FULL_ADS_rateapptext = "";
    public static String EXIT_FULL_ADS_rateurl = "";
    public static String EXIT_FULL_ADS_email = "";
    public static String EXIT_FULL_ADS_updateTYPE = "";
    public static String EXIT_FULL_ADS_appurl = "";
    public static String EXIT_FULL_ADS_prompttext = "";
    public static String EXIT_FULL_ADS_version = "";
    public static String EXIT_FULL_ADS_moreurl = "";
    public static String EXIT_FULL_ADS_src = "";
    public static String EXIT_SHOW_AD_ON_EXIT_PROMPT = "";

    public static List<AdsProviders> NATIVE_MEDIUM_providers = new ArrayList<>();
    //public static String NATIVE_MEDIUM_ad_id = "";
    //public static String NATIVE_MEDIUM_provider_id = "";
    public static String NATIVE_MEDIUM_clicklink = "";
    public static String NATIVE_MEDIUM_start_date = "";
    public static String NATIVE_MEDIUM_nevigation = "";
    public static String NATIVE_MEDIUM_call_native = "";
    public static String NATIVE_MEDIUM_rateapptext = "";
    public static String NATIVE_MEDIUM_rateurl = "";
    public static String NATIVE_MEDIUM_email = "";
    public static String NATIVE_MEDIUM_updateTYPE = "";
    public static String NATIVE_MEDIUM_appurl = "";
    public static String NATIVE_MEDIUM_prompttext = "";
    public static String NATIVE_MEDIUM_version = "";
    public static String NATIVE_MEDIUM_moreurl = "";
    public static String NATIVE_MEDIUM_src = "";

    public static List<AdsProviders> NATIVE_LARGE_providers = new ArrayList<>();
    //public static String NATIVE_LARGE_ad_id = "";
    //public static String NATIVE_LARGE_provider_id = "";
    public static String NATIVE_LARGE_clicklink = "";
    public static String NATIVE_LARGE_start_date = "";
    public static String NATIVE_LARGE_nevigation = "";
    public static String NATIVE_LARGE_call_native = "";
    public static String NATIVE_LARGE_rateapptext = "";
    public static String NATIVE_LARGE_rateurl = "";
    public static String NATIVE_LARGE_email = "";
    public static String NATIVE_LARGE_updateTYPE = "";
    public static String NATIVE_LARGE_appurl = "";
    public static String NATIVE_LARGE_prompttext = "";
    public static String NATIVE_LARGE_version = "";
    public static String NATIVE_LARGE_moreurl = "";
    public static String NATIVE_LARGE_src = "";

    public static String RATE_APP_ad_id = "";
    public static String RATE_APP_provider_id = "";
    public static String RATE_APP_clicklink = "";
    public static String RATE_APP_start_date = "";
    public static String RATE_APP_nevigation = "";
    public static String RATE_APP_call_native = "";
    public static String RATE_APP_rateapptext = "";
    public static String RATE_APP_rateurl = "";
    public static String RATE_APP_email = "";
    public static String RATE_APP_updateTYPE = "";
    public static String RATE_APP_appurl = "https://play.google.com/store/apps/developer?id=Quantum4u";
    public static String RATE_APP_prompttext = "";
    public static String RATE_APP_version = "";
    public static String RATE_APP_moreurl = "";
    public static String RATE_APP_src = "";
    public static String RATE_APP_HEADER_TEXT = "";
    public static String RATE_APP_BG_COLOR = "";
    public static String RATE_APP_TEXT_COLOR = "";

    public static String FEEDBACK_ad_id = "";
    public static String FEEDBACK_provider_id = "";
    public static String FEEDBACK_clicklink = "";
    public static String FEEDBACK_start_date = "";
    public static String FEEDBACK_nevigation = "";
    public static String FEEDBACK_call_native = "";
    public static String FEEDBACK_rateapptext = "";
    public static String FEEDBACK_rateurl = "";
    public static String FEEDBACK_email = "";
    public static String FEEDBACK_updateTYPE = "";
    public static String FEEDBACK_appurl = "";
    public static String FEEDBACK_prompttext = "";
    public static String FEEDBACK_version = "";
    public static String FEEDBACK_moreurl = "";
    public static String FEEDBACK_src = "";

    public static String UPDATES_ad_id = "";
    public static String UPDATES_provider_id = "";
    public static String UPDATES_clicklink = "";
    public static String UPDATES_start_date = "";
    public static String UPDATES_nevigation = "";
    public static String UPDATES_call_native = "";
    public static String UPDATES_rateapptext = "";
    public static String UPDATES_rateurl = "";
    public static String UPDATES_email = "";
    public static String UPDATES_updateTYPE = "";
    public static String UPDATES_appurl = "";
    public static String UPDATES_prompttext = "";
    public static String UPDATES_version = "";
    public static String UPDATES_moreurl = "";
    public static String UPDATES_ADS_src = "";

    public static String MOREAPP_ad_id = "";
    public static String MOREAPP_provider_id = "";
    public static String MOREAPP_clicklink = "";
    public static String MOREAPP_start_date = "";
    public static String MOREAPP_nevigation = "";
    public static String MOREAPP_call_native = "";
    public static String MOREAPP_rateapptext = "";
    public static String MOREAPP_rateurl = "";
    public static String MOREAPP_email = "";
    public static String MOREAPP_updateTYPE = "";
    public static String MOREAPP_appurl = "";
    public static String MOREAPP_prompttext = "";
    public static String MOREAPP_version = "";
    public static String MOREAPP_moreurl = "https://play.google.com/store/apps/developer?id=Quantum4u";
    public static String MOREAPP_ADS_src = "";

    public static String SHARE_URL = "";
    public static String SHARE_TEXT = "";


    public static String ETC_1 = "";
    public static String ETC_2 = "";
    public static String ETC_3 = "";
    public static String ETC_4 = "";
    public static String ETC_5 = "";


    public static String CP_cpname = "";
    public static String CP_navigation_count = "";
    public static String CP_is_start = "";
    public static String CP_is_exit = "";
    public static String CP_startday = "";
    public static String CP_package_name = "";
    public static String CP_camp_img = "";
    public static String CP_camp_click_link = "";

    public static String ABOUTDETAIL_DESCRIPTION = "";
    public static String ABOUTDETAIL_OURAPP = "";
    public static String ABOUTDETAIL_WEBSITELINK = "";
    public static String ABOUTDETAIL_PRIVACYPOLICY = "";
    public static String ABOUTDETAIL_TERM_AND_COND = "";
    public static String ABOUTDETAIL_FACEBOOK = "";
    public static String ABOUTDETAIL_INSTA = "";
    public static String ABOUTDETAIL_TWITTER = "";
    public static String ABOUTDETAIL_FAQ = "";
    public static String ABOUTDETAIL_ADS_START_DATE = "";

    public static String REMOVE_ADS_DESCRIPTION = "";
    public static String REMOVE_ADS_ADSLINK = "";
    public static String REMOVE_ADS_BGCOLOR = "";
    public static String REMOVE_ADS_TEXTCOLOR = "";
    public static String REMOVE_ADS_START_DATE = "";


    /*for non repetetive*/
    public static ArrayList<NonRepeatCount> EXIT_NON_REPEAT_COUNT = new ArrayList<>();

    /*for repetetive*/
    public static String EXIT_REPEAT_RATE = "";
    public static String EXIT_REPEAT_EXIT = "";
    public static String EXIT_REPEAT_FULL_ADS = "";
    public static String EXIT_REPEAT_REMOVEADS = "";

    /*for non repetetive*/
    public static ArrayList<LaunchNonRepeatCount> LAUNCH_NON_REPEAT_COUNT = new ArrayList<>();

    /*for repetetive*/
    public static String LAUNCH_REPEAT_RATE = "";
    public static String LAUNCH_REPEAT_EXIT = "";
    public static String LAUNCH_REPEAT_FULL_ADS = "";
    public static String LAUNCH_REPEAT_REMOVEADS = "";

    /*billing*/
    public static String INAPP_PUBLIC_KEY = "";

    /**
     * give status true if any of the inapp item, let it be monthly, weekly, half yearly, yearly or pro is purchased
     *
     * @param c context of the application
     * @return true is any of the features is purchased, other wise false
     */
    public static boolean hasPurchased(Context c) {
//        return (Slave.IS_PRO || Slave.IS_WEEKLY || Slave.IS_MONTHLY || Slave.IS_HALFYEARLY || Slave.IS_YEARLY) ? true : false;
        BillingPreference bf = new BillingPreference(c);
        return bf.isPro() || bf.isWeekly() || bf.isMonthly() || bf.isHalfYearly() || bf.isYearly() ? true : false;
//    return true;
    }

    public static boolean hasPurchasedWeekly(Context c) {
        BillingPreference bf = new BillingPreference(c);
        return bf.isWeekly() ? true : false;
    }

    public static boolean hasPurchasedMonthly(Context c) {
        BillingPreference bf = new BillingPreference(c);
        return bf.isMonthly();
    }

    public static boolean hasPurchasedHalfYearly(Context c) {
        BillingPreference bf = new BillingPreference(c);
        return bf.isHalfYearly();
    }

    public static boolean hasPurchasedYearly(Context c) {
        BillingPreference bf = new BillingPreference(c);
        return bf.isYearly();
    }

    public static boolean hasPurchasedPro(Context c) {
        BillingPreference bf = new BillingPreference(c);
        return bf.isPro();
    }

}

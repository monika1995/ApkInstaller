package app.enginev4;


import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

import app.listener.AppAdsListener;
import app.listener.AppFullAdsListener;
import app.server.v2.Slave;
import app.server.v4.AdsProviders;
import app.serviceprovider.AdMobAdaptive;
import app.serviceprovider.AdMobAds;
import app.serviceprovider.AdmobNativeAdvanced;
import app.serviceprovider.AppLovinAdsProvider;
import app.serviceprovider.FbAdsProvider;
import app.serviceprovider.InHouseAds;
import app.serviceprovider.StartupAdsProvider;
import app.serviceprovider.UnityAdsUtils;
import app.serviceprovider.Utils;
import app.serviceprovider.VungleAdsUtils;
import app.socket.EngineClient;
import app.ui.AdsLoadingActivity;

import static app.serviceprovider.Utils.getFullAdsCount;
import static app.serviceprovider.Utils.isNetworkConnected;
import static app.serviceprovider.Utils.setFulladsCount;


/**
 * Created by Meenu Singh on 05/06/19.
 */
public class AdsHelper {

    /**
     * here adMobSplashCache and fbSplashCache boolean are manage for not requesting multiple ads.
     */
    private boolean adMobSplashCache = false, fbSplashCache = false;

    private static AdsHelper instance;

    private AdsHelper() {

    }

    public static AdsHelper getInstance() {
        if (instance == null) {
            synchronized (AdsHelper.class) {
                if (instance == null) {
                    instance = new AdsHelper();
                }
            }
        }
        return instance;
    }

    public void getNewBannerFooter(final Activity context, final int position, AppAdsListener listener) {
        if (position >= Slave.BOTTOM_BANNER_providers.size()) {
            return;
        }
        AdsProviders providers = Slave.BOTTOM_BANNER_providers.get(position);
        Log.d("AdsHelper ", "NewEngine getNewBannerFooter " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        switch (providers.provider_id) {
            case Slave.Provider_Admob_Banner:
                //AdMobAds.getAdmobOBJ(context).admob_GetBannerAds(context, providers.ad_id, listener);
                AdMobAdaptive.getAdmobAdaptiveObj(context).admob_GetBannerAdaptive(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Facebook_Banner:
                FbAdsProvider.getFbObject().getFBBanner(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Inhouse_Banner:
                new InHouseAds().getBannerFooter(context, InHouseAds.TYPE_BANNER_FOOTER, listener);

                break;
            case Slave.Provider_Startapp_Banner:
                StartupAdsProvider.getStartappObj(context, providers.ad_id).getBannerAds(context, listener);

                break;
            case Slave.Provider_Unity_Banner:
                UnityAdsUtils.getUnityObj().getUnityAdsBanner(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Applovin_Banner:
                AppLovinAdsProvider.getAppLovinObject(context).getAppLovinBanner(context, listener);

                break;
            default:
                AdMobAds.getAdmobOBJ(context).admob_GetBannerAds(context, Slave.ADMOB_BANNER_ID_STATIC, listener);
                break;
        }

    }

    public void getNewBannerHeader(final Activity context, final int position, AppAdsListener listener) {
        if (position >= Slave.TOP_BANNER_providers.size()) {
            return;
        }

        AdsProviders providers = Slave.TOP_BANNER_providers.get(position);
        Log.d("AdsHelper ", "NewEngine getNewBannerHeader " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);


        switch (providers.provider_id) {
            case Slave.Provider_Admob_Banner:
                AdMobAds.getAdmobOBJ(context).admob_GetBannerAds(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Facebook_Banner:
                FbAdsProvider.getFbObject().getFBBanner(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Inhouse_Banner:
                new InHouseAds().getBannerHeader(context, InHouseAds.TYPE_BANNER_HEADER, listener);

                break;
            case Slave.Provider_Startapp_Banner:
                StartupAdsProvider.getStartappObj(context, providers.ad_id).getBannerAds(context, listener);

                break;
            case Slave.Provider_Unity_Banner:
                UnityAdsUtils.getUnityObj().getUnityAdsBanner(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Applovin_Banner:
                AppLovinAdsProvider.getAppLovinObject(context).getAppLovinBanner(context, listener);

                break;
            default:
                AdMobAds.getAdmobOBJ(context).admob_GetBannerAds(context, Slave.ADMOB_BANNER_ID_STATIC, listener);
                break;
        }

    }

    public void getNewBannerLarge(final Activity context, final int position, AppAdsListener listener) {
        if (position >= Slave.LARGE_BANNER_providers.size()) {
            return;
        }
        AdsProviders providers = Slave.LARGE_BANNER_providers.get(position);
        Log.d("AdsHelper ", "NewEngine getNewBannerLarge " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        switch (providers.provider_id) {
            case Slave.Provider_Admob_Banner_Large:
                AdMobAds.getAdmobOBJ(context).admob_GetBannerLargeAds(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Facebook_Banner_Large:
                FbAdsProvider.getFbObject().getFBBannerLarge(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Applovin_Banner_Large:
                AppLovinAdsProvider.getAppLovinObject(context).getAppLovinBannerLarge(context, listener);

                break;
            case Slave.Provider_Inhouse_Banner_Large:
                new InHouseAds().getBannerLarge(context, InHouseAds.TYPE_BANNER_LARGE, listener);

                break;
            default:
                AdMobAds.getAdmobOBJ(context).admob_GetBannerLargeAds(context, Slave.ADMOB_BANNER_ID_LARGE_STATIC, listener);
                break;
        }

    }

    public void getNewBannerRectangle(final Activity context, final int position, AppAdsListener listener) {
        if (position >= Slave.RECTANGLE_BANNER_providers.size()) {
            return;
        }
        AdsProviders providers = Slave.RECTANGLE_BANNER_providers.get(position);
        Log.d("AdsHelper ", "NewEngine getNewBannerRectangle " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        switch (providers.provider_id) {
            case Slave.Provider_Admob_Banner_Rectangle:
                AdMobAds.getAdmobOBJ(context).admob_GetBannerRectangleAds(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Facebook_Banner_Rect:
                FbAdsProvider.getFbObject().getFBBannerRectangle(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Vungle_Banner_Rect:
                VungleAdsUtils.getVungleObject().getVungleNativeAds(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Applovin_Banner_Rectangle:
                AppLovinAdsProvider.getAppLovinObject(context).getAppLovinBannerRectangle(context, listener);

                break;
            case Slave.Provider_Inhouse_Banner_Rect:
                new InHouseAds().getBannerRectangle(context, InHouseAds.TYPE_BANNER_RECTANGLE, listener);

                break;
            default:
                AdMobAds.getAdmobOBJ(context).admob_GetBannerRectangleAds(context, Slave.ADMOB_BANNER_ID_RECTANGLE_STATIC, listener);
                break;
        }

    }

    public void getNewNativeMedium(final Activity context, final int position, AppAdsListener listener) {
        if (position >= Slave.NATIVE_MEDIUM_providers.size()) {
            return;
        }
        AdsProviders providers = Slave.NATIVE_MEDIUM_providers.get(position);
        Log.d("AdsHelper ", "NewEngine getNewNativeMedium " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        switch (providers.provider_id) {
            case Slave.Provider_Admob_Native_Medium:
                //new AdmobNativeAdvanced().getNativeAdvancedAds(context, providers.ad_id, false, listener);
                AdmobNativeAdvanced.getInstance(context).showNativeAdvancedAds(context, providers.ad_id, false, listener);

                break;
            case Slave.Provider_Facebook_Native_Medium:
                FbAdsProvider.getFbObject().getNativeAds(context, false, providers.ad_id, listener);

                break;
            case Slave.Provider_Inhouse_Medium:
                new InHouseAds().showNativeMedium(context, InHouseAds.TYPE_NATIVE_MEDIUM, listener);

                break;
            case Slave.Provider_Startapp_Native_Medium:
                StartupAdsProvider.getStartappObj(context, providers.ad_id).showNativeMedium(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Applovin_Native_Medium:
                AppLovinAdsProvider.getAppLovinObject(context).showAppLovinNativeMedium(context, listener);

                break;
            default:
                //  new AdmobNativeAdvanced().getNativeAdvancedAds(context, Slave.ADMOB_NATIVE_MEDIUM_ID_STATIC, false, listener);
                AdmobNativeAdvanced.getInstance(context).showNativeAdvancedAds(context, Slave.ADMOB_NATIVE_MEDIUM_ID_STATIC, false, listener);

                break;
        }
    }

    public void getNewNativeLarge(final Activity context, final int position, AppAdsListener listener) {
        if (position >= Slave.NATIVE_LARGE_providers.size()) {
            return;
        }
        AdsProviders providers = Slave.NATIVE_LARGE_providers.get(position);
        Log.d("AdsHelper ", "NewEngine getNewNativeLarge " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        switch (providers.provider_id) {
            case Slave.Provider_Admob_Native_Large:
                // new AdmobNativeAdvanced().getNativeAdvancedAds(context, providers.ad_id, true, listener);
                AdmobNativeAdvanced.getInstance(context).showNativeAdvancedAds(context, providers.ad_id, true, listener);

                break;
            case Slave.Provider_Facebook_Native_Large:
                FbAdsProvider.getFbObject().getNativeAds(context, true, providers.ad_id, listener);

                break;
            case Slave.Provider_Inhouse_Large:
                new InHouseAds().showNativeLarge(context, InHouseAds.TYPE_NATIVE_LARGE, listener);

                break;
            case Slave.Provider_Startapp_Native_Large:
                StartupAdsProvider.getStartappObj(context, providers.ad_id).showNativeLarge(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Applovin_Native_Large:
                AppLovinAdsProvider.getAppLovinObject(context).showAppLovinNativeLarge(context, listener);

                break;
            default:
                //new AdmobNativeAdvanced().getNativeAdvancedAds(context, Slave.ADMOB_BANNER_ID_LARGE_STATIC, true, listener);
                AdmobNativeAdvanced.getInstance(context).showNativeAdvancedAds(context, Slave.ADMOB_BANNER_ID_LARGE_STATIC, true, listener);
                break;
        }

    }

    public void getNewNativeGrid(final Activity context, final int position, AppAdsListener listener, ViewGroup moreLayout) {
        if (position >= Slave.NATIVE_MEDIUM_providers.size()) {
            return;
        }
        AdsProviders providers = Slave.NATIVE_MEDIUM_providers.get(position);
        Log.d("AdsHelper ", "NewEngine getNewNativeGrid " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        switch (providers.provider_id) {
            case Slave.Provider_Admob_Native_Medium:
                // new AdmobNativeAdvanced().getNativeAdvancedAds_GridView_Ads(context, providers.ad_id, moreLayout, listener);
                AdmobNativeAdvanced.getInstance(context).showNativeGridAds(context, providers.ad_id, moreLayout, listener);
                break;
            case Slave.Provider_Facebook_Native_Medium:
                FbAdsProvider.getFbObject().getNativeAds_Grid(providers.ad_id, context, moreLayout, listener);

                break;
            case Slave.Provider_Inhouse_Medium:
                new InHouseAds().loadGridViewNativeAdsView(context, providers.ad_id, moreLayout, listener);

                break;
            case Slave.Provider_Startapp_Native_Medium:
                StartupAdsProvider.getStartappObj(context, providers.ad_id).showNativeMedium_Grid(context, providers.ad_id, moreLayout, listener);

                break;
            default:
                // new AdmobNativeAdvanced().getNativeAdvancedAds_GridView_Ads(context, Slave.ADMOB_NATIVE_MEDIUM_ID_STATIC, moreLayout, listener);
                AdmobNativeAdvanced.getInstance(context).showNativeGridAds(context, Slave.ADMOB_NATIVE_MEDIUM_ID_STATIC, moreLayout, listener);
                break;
        }
    }


    public void getNewNativeRectangle(final Activity context, final int position, AppAdsListener listener) {
        if (position >= Slave.NATIVE_MEDIUM_providers.size()) {
            return;
        }
        AdsProviders providers = Slave.NATIVE_MEDIUM_providers.get(position);
        Log.d("AdsHelper ", "NewEngine getNewNativeRectangle " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        switch (providers.provider_id) {
            case Slave.Provider_Admob_Native_Medium:
                AdmobNativeAdvanced.getInstance(context).showNativeRectangleAds(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Facebook_Native_Medium:
                FbAdsProvider.getFbObject().getNativeAds(context, false, providers.ad_id, listener);

                break;
            case Slave.Provider_Inhouse_Medium:
                new InHouseAds().showNativeMedium(context, InHouseAds.TYPE_NATIVE_MEDIUM, listener);

                break;
            case Slave.Provider_Startapp_Native_Medium:
                StartupAdsProvider.getStartappObj(context, providers.ad_id).showNativeMedium(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Applovin_Native_Medium:
                AppLovinAdsProvider.getAppLovinObject(context).showAppLovinNativeMedium(context, listener);

                break;
            default:
                AdmobNativeAdvanced.getInstance(context).showNativeRectangleAds(context, Slave.ADMOB_NATIVE_MEDIUM_ID_STATIC, listener);

                break;
        }
    }

    public void getNewLaunchCacheFullPageAd(Activity context, final int position, AppFullAdsListener listener) {
        if (position >= Slave.LAUNCH_FULL_ADS_providers.size()) {
            return;
        }
        AdsProviders providers = Slave.LAUNCH_FULL_ADS_providers.get(position);
        Log.d("AdsHelper ", "NewEngine getNewLaunchCacheFullPageAd " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        switch (providers.provider_id) {
            case Slave.Provider_Admob_FullAds:
                adMobSplashCache = true;
                AdMobAds.getAdmobOBJ(context).admob_InitFullAds(context, providers.ad_id, listener, true);

                break;
            case Slave.Provider_Facebook_Full_Page_Ads:
                fbSplashCache = true;
                FbAdsProvider.getFbObject().loadFBFullAds(providers.ad_id, context, listener, true);

                break;
            case Slave.Provider_Vungle_Full_Page_Ads:
                VungleAdsUtils.getVungleObject().loadVungleFullAds(context, providers.ad_id, listener, true);

                break;
            case Slave.Provider_Applovin_FullAds_Page_Ads:
                AppLovinAdsProvider.getAppLovinObject(context).loadAppLovinFullAds(context, listener, true);

                break;
            case Slave.Provider_Startapp_FullAds:
                StartupAdsProvider.getStartappObj(context, providers.ad_id).loadFullAds(listener);

                break;
            case Slave.Provider_Inhouse_FullAds:
                /*
                 * in Inhouse case no need to cache that's why we assume cache is loaded.
                 */
                if (isNetworkConnected(context)) {
                    listener.onFullAdLoaded();
                } else {
                    listener.onFullAdFailed(AdsEnum.FULL_ADS_INHOUSE, "Internet issue");
                }
                break;
            case Slave.Provider_Unity_Full_Page_Ads:
                listener.onFullAdFailed(AdsEnum.FULL_ADS_UNITY, "Context issue ,we cache on dashboard");

                break;
            default:
                AdMobAds.getAdmobOBJ(context).admob_InitFullAds(context, Slave.ADMOB_FULL_ID_STATIC, listener, true);
                break;
        }

    }

    public void getNewExitCacheFullPageAd(Activity context, final int position, AppFullAdsListener listener) {
        if (position >= Slave.EXIT_FULL_ADS_providers.size()) {
            return;
        }
        AdsProviders providers = Slave.EXIT_FULL_ADS_providers.get(position);
        Log.d("AdsHelper ", "NewEngine getNewExitCacheFullPageAd " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);


        switch (providers.provider_id) {
            case Slave.Provider_Admob_FullAds:
                if (!adMobSplashCache) {
                    AdMobAds.getAdmobOBJ(context).admob_InitFullAds(context, providers.ad_id, listener, false);
                }

                break;
            case Slave.Provider_Facebook_Full_Page_Ads:
                if (!fbSplashCache) {
                    FbAdsProvider.getFbObject().loadFBFullAds(providers.ad_id, context, listener, false);
                }

                break;
            case Slave.Provider_Vungle_Full_Page_Ads:
                VungleAdsUtils.getVungleObject().loadVungleFullAds(context, providers.ad_id, listener, false);

                break;
            case Slave.Provider_Unity_Full_Page_Ads:
                listener.onFullAdFailed(AdsEnum.FULL_ADS_UNITY, "Context issue ,we cache on dashboard");

                break;
            case Slave.Provider_Applovin_FullAds_Page_Ads:
                AppLovinAdsProvider.getAppLovinObject(context).loadAppLovinFullAds(context, listener, false);

                break;
            case Slave.Provider_Startapp_FullAds:
                StartupAdsProvider.getStartappObj(context, providers.ad_id).loadFullAds(listener);

                break;
            case Slave.Provider_Inhouse_FullAds:
                /*
                 * in Inhouse case no need to cache that's why we assume cache is loaded.
                 */
                if (isNetworkConnected(context)) {
                    listener.onFullAdLoaded();
                } else {
                    listener.onFullAdFailed(AdsEnum.FULL_ADS_INHOUSE, "Internet issue");
                }
                break;
            default:
                AdMobAds.getAdmobOBJ(context).admob_InitFullAds(context, Slave.ADMOB_FULL_ID_STATIC, listener, false);
                break;
        }

    }

    public void getNewNavCacheFullPageAd(Activity context, final int position, AppFullAdsListener listener) {
        if (position >= Slave.FULL_ADS_providers.size()) {
            return;
        }
        AdsProviders providers = Slave.FULL_ADS_providers.get(position);
        Log.d("AdsHelper ", "NewEngine getNewNavCacheFullPageAd " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        switch (providers.provider_id) {
            case Slave.Provider_Admob_FullAds:
                if (!adMobSplashCache) {
                    AdMobAds.getAdmobOBJ(context).admob_InitFullAds(context, providers.ad_id, listener, true);
                }

                break;
            case Slave.Provider_Facebook_Full_Page_Ads:
                if (!fbSplashCache) {
                    FbAdsProvider.getFbObject().loadFBFullAds(providers.ad_id, context, listener, true);
                }

                break;
            case Slave.Provider_Unity_Full_Page_Ads:
                UnityAdsUtils.getUnityObj().loadUnityFullAds(context, providers.ad_id, listener, true);

                break;
            case Slave.Provider_Vungle_Full_Page_Ads:
                VungleAdsUtils.getVungleObject().loadVungleFullAds(context, providers.ad_id, listener, true);

                break;
            case Slave.Provider_Applovin_FullAds_Page_Ads:
                AppLovinAdsProvider.getAppLovinObject(context).loadAppLovinFullAds(context, listener, true);

                break;
            case Slave.Provider_Startapp_FullAds:
                StartupAdsProvider.getStartappObj(context, providers.ad_id).loadFullAds(listener);

                break;
            case Slave.Provider_Inhouse_FullAds:
                /*
                 * in Inhouse case no need to cache that's why we assume cache is loaded.
                 */
                if (isNetworkConnected(context)) {
                    listener.onFullAdLoaded();
                } else {
                    listener.onFullAdFailed(AdsEnum.FULL_ADS_INHOUSE, "Internet issue");
                }
                break;
            default:
                AdMobAds.getAdmobOBJ(context).admob_InitFullAds(context, Slave.ADMOB_FULL_ID_STATIC, listener, true);
                break;
        }

    }

    public void showFullAdsOnLaunch(final Activity context, final int position, final AppFullAdsListener listener) {
        if (position >= Slave.LAUNCH_FULL_ADS_providers.size()) {
            return;
        }
        final AdsProviders providers = Slave.LAUNCH_FULL_ADS_providers.get(position);
        Log.d("AdsHelper ", "NewEngine showFullAdsOnLaunch " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.LAUNCH_FULL_ADS_start_date)) {
            switch (providers.provider_id) {
                case Slave.Provider_Admob_FullAds:
                    AdMobAds.getAdmobOBJ(context).admob_showFullAds(context, providers.ad_id, listener);

                    break;
                case Slave.Provider_Facebook_Full_Page_Ads:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FbAdsProvider.getFbObject().showFBFullAds(providers.ad_id, context, listener);
                        }
                    }, 2200);
                    if (isNetworkConnected(context)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                context.startActivity(new Intent(context, AdsLoadingActivity.class));
                            }
                        }, 1000);
                    }

                    break;
                case Slave.Provider_Startapp_FullAds:
                    StartupAdsProvider.getStartappObj(context, providers.ad_id).showfullAds(context, Utils.getStringtoInt(providers.ad_id), listener);

                    break;
                case Slave.Provider_Inhouse_FullAds:
                    if (isNetworkConnected(context)) {
                        Slave.LAUNCH_FULL_ADS_src = providers.src;
                        Slave.LAUNCH_FULL_ADS_clicklink = providers.clicklink;
                        new InHouseAds().showFullAds(context, EngineClient.IH_LAUNCH_FULL, Slave.LAUNCH_FULL_ADS_src, Slave.LAUNCH_FULL_ADS_clicklink, listener);
                    }

                    break;
                case Slave.Provider_Unity_Full_Page_Ads:
                    UnityAdsUtils.getUnityObj().loadUnityFullAds(context, providers.ad_id, listener, true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            UnityAdsUtils.getUnityObj().showUnityFullAds(context, providers.ad_id, listener);
                        }
                    }, 6000);
                    if (isNetworkConnected(context)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                context.startActivity(new Intent(context, AdsLoadingActivity.class).putExtra("timer",6000));
                            }
                        }, 500);
                    }

                    break;
                case Slave.Provider_Vungle_Full_Page_Ads:
                    VungleAdsUtils.getVungleObject().showVungleFullAds(context, providers.ad_id, listener);

                    break;
                case Slave.Provider_Applovin_FullAds_Page_Ads:
                    AppLovinAdsProvider.getAppLovinObject(context).showAppLovinFullAds(context, listener);


                    break;
                default:
                    AdMobAds.getAdmobOBJ(context).admob_showFullAds(context, Slave.ADMOB_FULL_ID_STATIC, listener);
                    break;
            }

        }
    }

    public void showFullAdsOnExit(final Activity context, final int position, final AppFullAdsListener listener) {
        if (position >= Slave.EXIT_FULL_ADS_providers.size()) {
            return;
        }
        final AdsProviders providers = Slave.EXIT_FULL_ADS_providers.get(position);
        Log.d("AdsHelper ", "NewEngine showFullAdsOnExit " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.EXIT_FULL_ADS_start_date)) {

            switch (providers.provider_id) {
                case Slave.Provider_Admob_FullAds:
                    AdMobAds.getAdmobOBJ(context).admob_showFullAds(context, providers.ad_id, listener);

                    break;
                case Slave.Provider_Facebook_Full_Page_Ads:
                    FbAdsProvider.getFbObject().showFBFullAds(providers.ad_id, context, listener);

                    break;
                case Slave.Provider_Startapp_FullAds:
                    StartupAdsProvider.getStartappObj(context, providers.ad_id).showfullAds(context, Utils.getStringtoInt(providers.ad_id), listener);

                    break;
                case Slave.Provider_Inhouse_FullAds:
                    if (isNetworkConnected(context)) {
                        Slave.EXIT_FULL_ADS_src = providers.src;
                        Slave.EXIT_FULL_ADS_clicklink = providers.clicklink;
                        new InHouseAds().showFullAds(context, EngineClient.IH_EXIT_FULL_ADS, Slave.EXIT_FULL_ADS_src, Slave.EXIT_FULL_ADS_clicklink, listener);
                    }
                    break;
                case Slave.Provider_Unity_Full_Page_Ads:
                    UnityAdsUtils.getUnityObj().showUnityFullAds(context, providers.ad_id, listener);
                   /* UnityAdsUtils.getUnityObj().loadUnityFullAds(context, providers.ad_id, listener, true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            UnityAdsUtils.getUnityObj().showUnityFullAds(context, providers.ad_id, listener);
                        }
                    }, 2200);
                    if (isNetworkConnected(context)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                context.startActivity(new Intent(context, AdsLoadingActivity.class));
                            }
                        }, 1000);
                    }*/

                    break;
                case Slave.Provider_Vungle_Full_Page_Ads:
                    VungleAdsUtils.getVungleObject().showVungleFullAds(context, providers.ad_id, listener);

                    break;
                case Slave.Provider_Applovin_FullAds_Page_Ads:
                    AppLovinAdsProvider.getAppLovinObject(context).showAppLovinFullAds(context, listener);

                    break;
                default:
                    AdMobAds.getAdmobOBJ(context).admob_showFullAds(context, Slave.ADMOB_FULL_ID_STATIC, listener);
                    break;
            }

        }
    }

    public void showForcedFullAds(final Activity context, final int position, AppFullAdsListener listener) {
        if (position >= Slave.FULL_ADS_providers.size()) {
            return;
        }
        AdsProviders providers = Slave.FULL_ADS_providers.get(position);
        Log.d("AdsHelper ", "NewEngine showForcedFullAds " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);

        switch (providers.provider_id) {
            case Slave.Provider_Admob_FullAds:
                AdMobAds.getAdmobOBJ(context).admob_showFullAds(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Facebook_Full_Page_Ads:
                FbAdsProvider.getFbObject().showFBFullAds(providers.ad_id, context, listener);

                break;
            case Slave.Provider_Startapp_FullAds:
                StartupAdsProvider.getStartappObj(context, providers.ad_id).showfullAds(context, Utils.getStringtoInt(providers.ad_id), listener);

                break;
            case Slave.Provider_Inhouse_FullAds:
                if (isNetworkConnected(context)) {
                    Slave.FULL_ADS_src = providers.src;
                    Slave.FULL_ADS_clicklink = providers.clicklink;
                    new InHouseAds().showFullAds(context, EngineClient.IH_FULL, Slave.FULL_ADS_src, Slave.FULL_ADS_clicklink, listener);
                }
                break;
            case Slave.Provider_Unity_Full_Page_Ads:
                UnityAdsUtils.getUnityObj().loadUnityFullAds(context, providers.ad_id, listener, true);
                UnityAdsUtils.getUnityObj().showUnityFullAds(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Vungle_Full_Page_Ads:
                VungleAdsUtils.getVungleObject().loadVungleFullAds(context, providers.ad_id, listener, true);
                VungleAdsUtils.getVungleObject().showVungleFullAds(context, providers.ad_id, listener);


                break;
            case Slave.Provider_Applovin_FullAds_Page_Ads:
                AppLovinAdsProvider.getAppLovinObject(context).loadAppLovinFullAds(context, listener, false);
                AppLovinAdsProvider.getAppLovinObject(context).showAppLovinFullAds(context, listener);

                break;
            default:
                if (getFullAdsCount(context) >= Utils.getStringtoInt(Slave.FULL_ADS_nevigation)) {
                    setFulladsCount(context, 0);
                    AdMobAds.getAdmobOBJ(context).admob_showFullAds(context, Slave.ADMOB_FULL_ID_STATIC, listener);
                }
                break;
        }
    }

    public void showFullAds(final Activity context, final int position, AppFullAdsListener listener) {
        if (position >= Slave.FULL_ADS_providers.size()) {
            return;
        }
        final AdsProviders providers = Slave.FULL_ADS_providers.get(position);
        Log.d("AdsHelper ", "NewEngine showFullAds  navigation " + position
                + " " + providers.provider_id
                + " " + providers.ad_id);


        switch (providers.provider_id) {
            case Slave.Provider_Admob_FullAds:
                AdMobAds.getAdmobOBJ(context).admob_showFullAds(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Facebook_Full_Page_Ads:
                FbAdsProvider.getFbObject().showFBFullAds(providers.ad_id, context, listener);

                break;
            case Slave.Provider_Startapp_FullAds:
                StartupAdsProvider.getStartappObj(context, providers.ad_id).showfullAds(context, Utils.getStringtoInt(providers.ad_id), listener);

                break;
            case Slave.Provider_Inhouse_FullAds:
                if (isNetworkConnected(context)) {
                    Slave.FULL_ADS_src = providers.src;
                    Slave.FULL_ADS_clicklink = providers.clicklink;
                    new InHouseAds().showFullAds(context, EngineClient.IH_FULL, Slave.FULL_ADS_src, Slave.FULL_ADS_clicklink, listener);
                }
                break;
            case Slave.Provider_Unity_Full_Page_Ads:
                UnityAdsUtils.getUnityObj().showUnityFullAds(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Vungle_Full_Page_Ads:
                VungleAdsUtils.getVungleObject().showVungleFullAds(context, providers.ad_id, listener);

                break;
            case Slave.Provider_Applovin_FullAds_Page_Ads:
                AppLovinAdsProvider.getAppLovinObject(context).showAppLovinFullAds(context, listener);

                break;
            default:
                AdMobAds.getAdmobOBJ(context).admob_showFullAds(context, Slave.ADMOB_FULL_ID_STATIC, listener);

                break;
        }

    }

    public void onAHandlerDestroy() {
        FbAdsProvider.getFbObject().FbAdsDestroy();
        UnityAdsUtils.getUnityObj().UnityAdsDestroy();
    }


}

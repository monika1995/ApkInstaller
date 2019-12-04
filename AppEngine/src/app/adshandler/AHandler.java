package app.adshandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import app.PrintLog;
import app.enginev4.AdsEnum;
import app.enginev4.AdsHelper;
import app.enginev4.LoadAdData;
import app.fcm.MapperUtils;
import app.inapp.BillingListActivity;
import app.listener.AppAdsListener;
import app.listener.AppFullAdsListener;
import app.listener.OnCacheFullAdLoaded;
import app.pnd.adshandler.R;
import app.server.v2.DataHubConstant;
import app.server.v2.DataHubPreference;
import app.server.v2.MoreFeature;
import app.server.v2.MoreFeatureResponseHandler;
import app.server.v2.Slave;
import app.serviceprovider.Utils;
import app.socket.EngineClient;
import app.ui.ExitAdsActivity;


public class AHandler {
    private static AHandler instance;
    private PromptHander promptHander;

    private AHandler() {
        promptHander = new PromptHander();
    }

    public static AHandler getInstance() {
        if (instance == null) {
            synchronized (AHandler.class) {
                if (instance == null) {
                    instance = new AHandler();
                }
            }
        }
        return instance;
    }

    /**
     * calling from app launcher class
     */
    public void v2CallOnSplash(final Context context, final OnCacheFullAdLoaded l) {
        DataHubPreference dP = new DataHubPreference(context);

        dP.setAppName(Utils.getAppName(context));
        DataHubConstant.APP_LAUNCH_COUNT = Integer.parseInt(dP.getAppLaunchCount());

        EngineHandler engineHandler = new EngineHandler(context);
        engineHandler.initServices(false);
        //cacheFullPageAd((Activity) context, l);
        engineHandler.initServices(true);
        /*
         * cache are working on navigation ..
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleLaunchCache((Activity) context, l);
            }
        }, 3000);
    }

    /**
     * calling from app dashboard
     */
    public void v2CallonAppLaunch(final Activity context) {
        if (!Slave.hasPurchased(context)) {

            PrintLog.print("CHECK CHECK 1 PRO" + " " + Slave.IS_PRO);
            PrintLog.print("CHECK CHECK 2 WEEKLY" + " " + Slave.IS_WEEKLY);
            PrintLog.print("CHECK CHECK 3 MONTHLY" + " " + Slave.IS_MONTHLY);
            PrintLog.print("CHECK CHECK 4 HALF_YEARLY" + " " + Slave.IS_HALFYEARLY);
            PrintLog.print("CHECK CHECK 5 YEARLY" + " " + Slave.IS_YEARLY);
            PrintLog.print("here inside applaunch 02");

            handle_launch_prompt(context);
            cacheNavigationFullAd(context);
        }


        if (promptHander == null) {
            promptHander = new PromptHander();
        }
        promptHander.checkForForceUpdate(context);
        promptHander.checkForNormalUpdate(context);

        EngineHandler engineHandler = new EngineHandler(context);
        engineHandler.doGCMRequest();
        engineHandler.doTopicsRequest();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callingForMapper(context);
            }
        }, 2000);
    }

    /**
     * @param activity call this function when app launch from background
     */
    public void v2CallOnBGLaunch(Activity activity) {
        EngineHandler engineHandler = new EngineHandler(activity);
        engineHandler.initServices(false);

        cacheLaunchFullAd(activity, new OnCacheFullAdLoaded() {
            @Override
            public void onCacheFullAd() {

            }
        });
        Utils.setFulladsCount(activity, Utils.getStringtoInt(Slave.FULL_ADS_nevigation));


    }

    /**
     * @param context exit prompt
     */
    public void v2ManageAppExit(Activity context) {
        if (!Slave.hasPurchased(context)) {
            if (Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT.equals("false")) {
                cacheExitFullAd(context);
            }
            handle_exit_prompt(context);
        }
    }

    /**
     * open about us page
     */
    public void showAboutUs(Activity mContext) {
        mContext.startActivity(new Intent(mContext, new DataHubConstant(mContext).showAboutUsPage()));
    }

    /**
     * call to show exit prompt
     */
    public void showExitPrompt(Activity mContext) {
        mContext.startActivity(new Intent(mContext, ExitAdsActivity.class));
    }

    /**
     * open billing activity
     */
    public void showRemoveAdsPrompt(Context mContext) {
        Intent intent = new Intent(mContext, BillingListActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * open FAQs customtab
     */
    public void showFAQs(Activity mContext) {
        Utils.showFAQs(mContext);
    }


    /**
     * show banner footer ads
     */
    public View getBannerFooter(Activity context) {
        if (Slave.hasPurchased(context)) {
            return new LinearLayout(context);
        }
        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.BOTTOM_BANNER_start_date)) {

            if (Slave.TYPE_BOTTOM_BANNER.equalsIgnoreCase(Slave.BOTTOM_BANNER_call_native)) {
                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadBannerFooter(context, loadAdData, appAdContainer);

                return appAdContainer;

            } else if (Slave.TYPE_BANNER_LARGE.equalsIgnoreCase(Slave.BOTTOM_BANNER_call_native)) {
                return getBannerLarge(context);

            }

        }
        return new LinearLayout(context);
    }

    private void loadBannerFooter(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewBannerFooter(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler", "NewEngine getNewBannerFooter onAdFailed " + pos + " provider name " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                loadBannerFooter(context, loadAdData, ll);
            }
        });

    }

    /**
     * show banner header ads
     */
    public View getBannerHeader(Activity context) {
        if (Slave.hasPurchased(context)) {
            return new LinearLayout(context);
        }
        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.TOP_BANNER_start_date)) {
            if (Slave.TYPE_TOP_BANNER.equalsIgnoreCase(Slave.TOP_BANNER_call_native)) {
                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadBannerHeader(context, loadAdData, appAdContainer);

                return appAdContainer;
            }

            if (Slave.TYPE_BANNER_LARGE.equalsIgnoreCase(Slave.TOP_BANNER_call_native)) {
                return getBannerLarge(context);
            }

        }
        return new LinearLayout(context);

    }

    private void loadBannerHeader(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewBannerHeader(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler", "NewEngine getNewBannerHeader onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                loadBannerHeader(context, loadAdData, ll);
            }
        });

    }

    /**
     * show banner large ads
     */
    public View getBannerLarge(Activity context) {
        if (Slave.hasPurchased(context)) {
            return new LinearLayout(context);
        }

        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.LARGE_BANNER_start_date)) {
            if (Slave.BANNER_TYPE_LARGE.equalsIgnoreCase(Slave.LARGE_BANNER_call_native)) {
                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadBannerLarge(context, loadAdData, appAdContainer);

                return appAdContainer;

            } else if (Slave.BANNER_TYPE_HEADER.equalsIgnoreCase(Slave.LARGE_BANNER_call_native)) {
                return getBannerHeader(context);

            }

        }
        return new LinearLayout(context);
    }

    private void loadBannerLarge(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewBannerLarge(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler", "NewEngine getNewBannerLarge onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                loadBannerLarge(context, loadAdData, ll);
            }
        });

    }

    /**
     * show banner rectangle ads
     */
    public View getBannerRectangle(Activity context) {
        if (Slave.hasPurchased(context)) {
            return new LinearLayout(context);
        }
        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.RECTANGLE_BANNER_start_date)) {

            if (Slave.BANNER_TYPE_RECTANGLE.equalsIgnoreCase(Slave.RECTANGLE_BANNER_call_native)) {
                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadBannerRectangle(context, loadAdData, appAdContainer);

                return appAdContainer;

            } else if (Slave.NATIVE_TYPE_MEDIUM.equalsIgnoreCase(Slave.RECTANGLE_BANNER_call_native)) {
                //return getNativeMedium(context);
                return getNativeRectangle(context);
            }
        }
        return new LinearLayout(context);

    }

    private void loadBannerRectangle(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewBannerRectangle(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler", "NewEngine getNewBannerRectangle onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                loadBannerRectangle(context, loadAdData, ll);
            }
        });

    }

    /**
     * show native medium ads
     */
    public View getNativeRectangle(Activity context) {
        if (Slave.hasPurchased(context)) {
            return new LinearLayout(context);
        }


        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.NATIVE_MEDIUM_start_date)) {

            if (Slave.NATIVE_TYPE_MEDIUM.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadNativeRectangle(context, loadAdData, appAdContainer);

                return appAdContainer;

            } else if (Slave.NATIVE_TYPE_LARGE.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getNativeLarge(context);

            } else if (Slave.TYPE_TOP_BANNER.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getBannerHeader(context);

            }
        }
        return new LinearLayout(context);

    }

    private void loadNativeRectangle(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewNativeRectangle(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler ", "NewEngine getNewNativeRectangle onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                loadNativeMedium(context, loadAdData, ll);
            }
        });

    }

    /**
     * show native large ads
     */
    public View getNativeLarge(Activity context) {
        if (Slave.hasPurchased(context)) {
            return new LinearLayout(context);
        }
        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.NATIVE_LARGE_start_date)) {

            if (Slave.NATIVE_TYPE_LARGE.equalsIgnoreCase(Slave.NATIVE_LARGE_call_native)) {
                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadNativeLarge(context, loadAdData, appAdContainer);

                return appAdContainer;

            } else if (Slave.NATIVE_TYPE_MEDIUM.equalsIgnoreCase(Slave.NATIVE_LARGE_call_native)) {
                return getNativeMedium(context);

            } else if (Slave.TYPE_TOP_BANNER.equalsIgnoreCase(Slave.NATIVE_LARGE_call_native)) {
                return getBannerHeader(context);

            }
        }

        return new LinearLayout(context);
    }

    private void loadNativeLarge(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewNativeLarge(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler ", "NewEngine getNewNativeLarge onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                loadNativeLarge(context, loadAdData, ll);
            }
        });

    }

    /**
     * show native grid ads and using native medium ads id
     */
    public View getNativeGrid(Activity context, ViewGroup morelayout) {
        if (Slave.hasPurchased(context)) {
            return new LinearLayout(context);
        }

        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.NATIVE_MEDIUM_start_date)) {
            if (Slave.NATIVE_TYPE_MEDIUM.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadNativeGrid(context, loadAdData, appAdContainer, morelayout);

                return appAdContainer;

            } else if (Slave.NATIVE_TYPE_LARGE.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getNativeLarge(context);

            } else if (Slave.TYPE_TOP_BANNER.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getBannerHeader(context);

            }
        }

        return new LinearLayout(context);
    }

    private void loadNativeGrid(final Activity context, final LoadAdData loadAdData, final ViewGroup ll, final ViewGroup moreLayout) {
        AdsHelper.getInstance().getNewNativeGrid(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler ", "NewEngine getNewNativeGrid onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                loadNativeGrid(context, loadAdData, ll, moreLayout);
            }
        }, moreLayout);

    }


    /**
     * show native rectangle ads
     */
    public View getNativeMedium(Activity context) {
        if (Slave.hasPurchased(context)) {
            return new LinearLayout(context);
        }


        if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.NATIVE_MEDIUM_start_date)) {

            if (Slave.NATIVE_TYPE_MEDIUM.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                LinearLayout appAdContainer = new LinearLayout(context);
                appAdContainer.setGravity(Gravity.CENTER);
                LoadAdData loadAdData = new LoadAdData();
                loadAdData.setPosition(0);
                loadNativeMedium(context, loadAdData, appAdContainer);

                return appAdContainer;

            } else if (Slave.NATIVE_TYPE_LARGE.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getNativeLarge(context);

            } else if (Slave.TYPE_TOP_BANNER.equalsIgnoreCase(Slave.NATIVE_MEDIUM_call_native)) {
                return getBannerHeader(context);

            }
        }
        return new LinearLayout(context);

    }

    private void loadNativeMedium(final Activity context, final LoadAdData loadAdData, final ViewGroup ll) {
        AdsHelper.getInstance().getNewNativeMedium(context, loadAdData.getPosition(), new AppAdsListener() {
            @Override
            public void onAdLoaded(View adsView) {
                if (ll != null) {
                    ll.removeAllViews();
                    ll.addView(adsView);
                }
            }

            @Override
            public void onAdFailed(AdsEnum providerName, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                Log.d("AHandler ", "NewEngine getNewNativeMedium onAdFailed " + pos + " " + providerName + " msg " + errorMsg);
                loadAdData.setPosition(pos);
                loadNativeMedium(context, loadAdData, ll);
            }
        });

    }


    /*
     * cache are working on navigation ..
     */
    private void handleLaunchCache(Activity context, OnCacheFullAdLoaded l) {
        try {
            int full_nonRepeat;
            PrintLog.print("cacheHandle >>1" + " " + DataHubConstant.APP_LAUNCH_COUNT);
            if (Slave.LAUNCH_NON_REPEAT_COUNT != null && Slave.LAUNCH_NON_REPEAT_COUNT.size() > 0) {
                for (int i = 0; i < Slave.LAUNCH_NON_REPEAT_COUNT.size(); i++) {
                    full_nonRepeat = Utils.getStringtoInt(Slave.LAUNCH_NON_REPEAT_COUNT.get(i).launch_full);

                    PrintLog.print("cacheHandle >>2" + " launchCount = " + DataHubConstant.APP_LAUNCH_COUNT + " | launchAdsCount = " + full_nonRepeat);

                    if (DataHubConstant.APP_LAUNCH_COUNT == full_nonRepeat) {
                        PrintLog.print("cacheHandle >>3" + " " + full_nonRepeat);
                        cacheLaunchFullAd(context, l);
                        return;
                    }
                }
            }
            PrintLog.print("cacheHandle >>4" + " " + Slave.LAUNCH_REPEAT_FULL_ADS);
            if (Slave.LAUNCH_REPEAT_FULL_ADS != null && !Slave.LAUNCH_REPEAT_FULL_ADS.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.LAUNCH_REPEAT_FULL_ADS) == 0) {
                PrintLog.print("cacheHandle >>5" + " " + Slave.LAUNCH_REPEAT_FULL_ADS);
                cacheLaunchFullAd(context, l);
            }

        } catch (Exception e) {
            PrintLog.print("cacheHandle excep ");
        }

    }


    /**
     * cache Launch full ads
     */
    private void cacheLaunchFullAd(Activity activity, OnCacheFullAdLoaded listener) {
        if (Slave.hasPurchased(activity)) {
            return;
        }
        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        loadLaunchCacheFullAds(activity, loadAdData, listener);
    }

    private void loadLaunchCacheFullAds(final Activity context, final LoadAdData loadAdData, final OnCacheFullAdLoaded listener) {
        AdsHelper.getInstance().getNewLaunchCacheFullPageAd(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                if (listener != null) {
                    listener.onCacheFullAd();
                }
                System.out.println("AHandler.onFullAdLoaded");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadLaunchCacheFullAds(context, loadAdData, listener);
                Log.d("AHandler", "NewEngine loadLaunchCacheFullAds onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }
        });

    }

    /**
     * show full ads on launch
     */
    private void showFullAdsOnLaunch(Activity activity) {
        if (Slave.hasPurchased(activity)) {
            return;
        }
        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        loadFullAdsOnLaunch(activity, loadAdData);
    }

    private void loadFullAdsOnLaunch(final Activity context, final LoadAdData loadAdData) {
        AdsHelper.getInstance().showFullAdsOnLaunch(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadFullAdsOnLaunch(context, loadAdData);
                Log.d("AHandler", "NewEngine loadFullAdsOnLaunch onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }
        });

    }

    /**
     * cache Exit full ads
     */
    private void cacheExitFullAd(Activity activity) {
        if (Slave.hasPurchased(activity)) {
            return;
        }
        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        loadExitCacheFullAds(activity, loadAdData);
    }

    private void loadExitCacheFullAds(final Activity context, final LoadAdData loadAdData) {
        AdsHelper.getInstance().getNewExitCacheFullPageAd(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                System.out.println("AHandler.onFullAdLoaded Exit");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadExitCacheFullAds(context, loadAdData);
                Log.d("AHandler", "NewEngine loadExitCacheFullAds onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }
        });

    }

    /**
     * show full ads on exit
     */
    private void showFullAdsOnExit(Activity activity) {
        if (Slave.hasPurchased(activity)) {
            return;
        }
        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        loadFullAdsOnExit(activity, loadAdData);
    }

    private void loadFullAdsOnExit(final Activity context, final LoadAdData loadAdData) {
        AdsHelper.getInstance().showFullAdsOnExit(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {

            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadFullAdsOnExit(context, loadAdData);
                Log.d("AHandler", "NewEngine loadFullAdsOnExit onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }
        });

    }

    /**
     * cache full ads
     */
    private void cacheNavigationFullAd(Activity activity) {
        if (Slave.hasPurchased(activity)) {
            return;
        }
        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        loadNavigationCacheFullAds(activity, loadAdData);
    }

    private void loadNavigationCacheFullAds(final Activity context, final LoadAdData loadAdData) {
        AdsHelper.getInstance().getNewNavCacheFullPageAd(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                System.out.println("AHandler.onFullAdLoaded loadNavigationCacheFullAds");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadNavigationCacheFullAds(context, loadAdData);
                Log.d("AHandler", "NewEngine loadNavigationCacheFullAds onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }
        });

    }

    /**
     * show full ads forcefully or not depend on isForced boolean
     */
    public void showFullAds(Activity activity, boolean isForced) {
        if (Slave.hasPurchased(activity)) {
            return;
        }
        LoadAdData loadAdData = new LoadAdData();
        loadAdData.setPosition(0);
        Log.d("AHandler", "NewEngine showFullAds getFullAdsCount " + Utils.getFullAdsCount(activity) + " FULL_ADS_nevigation " + Utils.getStringtoInt(Slave.FULL_ADS_nevigation));

        if (Utils.getDaysDiff(activity) >= Utils.getStringtoInt(Slave.FULL_ADS_start_date)) {
            Utils.setFulladsCount(activity, -1);

            if (!isForced) {
                if (Utils.getFullAdsCount(activity) >= Utils.getStringtoInt(Slave.FULL_ADS_nevigation)) {
                    Utils.setFulladsCount(activity, 0);
                    loadFullAds(activity, loadAdData);
                }
            } else {
                loadForceFullAds(activity, loadAdData);
            }
        }
    }

    /**
     * load force full ads
     */
    private void loadForceFullAds(final Activity context, final LoadAdData loadAdData) {
        AdsHelper.getInstance().showForcedFullAds(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {
                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadForceFullAds(context, loadAdData);
                Log.d("AHandler", "NewEngine loadForceFullAds onAdFailed " + pos + " " + adsEnum + " msg " + errorMsg);
            }
        });


    }

    /**
     * load full ads
     */
    private void loadFullAds(final Activity context, final LoadAdData loadAdData) {
        AdsHelper.getInstance().showFullAds(context, loadAdData.getPosition(), new AppFullAdsListener() {
            @Override
            public void onFullAdLoaded() {
                Log.d("AHandler", "NewEngine  showFullAds onFullAdLoaded");
            }

            @Override
            public void onFullAdFailed(AdsEnum adsEnum, String errorMsg) {

                int pos = loadAdData.getPosition();
                pos++;
                loadAdData.setPosition(pos);
                loadFullAds(context, loadAdData);
                Log.d("AHandler", "NewEngine  showFullAds onFullAdFailed " + loadAdData.getPosition() + " " + adsEnum.name() + " msg " + errorMsg);
            }
        });
    }

    /**
     * show cp ads on start
     */
    private void showCPStart(Activity context) {
        if (!Slave.hasPurchased(context)) {
            PrintLog.print("ding check inside 3 cp start");
            if (Slave.CP_is_start.equals(Slave.CP_YES)) {
                if (Utils.isPackageInstalled(Slave.CP_package_name, context)) {
                    PrintLog.print("ding check inside 4 cp start" + Slave.CP_startday);
                    if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.CP_startday)) {
                        PrintLog.print("ding check inside 5 cp start");

                        if (Utils.isNetworkConnected(context)) {
                            PrintLog.print("ding check inside 6 cp start");
                            Intent intent = new Intent(context, FullPagePromo.class);
                            intent.putExtra("src", Slave.CP_camp_img);
                            intent.putExtra("type", EngineClient.IH_CP_START);
                            intent.putExtra("link", Slave.CP_camp_click_link);
                            context.startActivity(intent);
                        }
                    }
                }
            }
        }
    }

    /**
     * show cp ads on exit
     */
    private void showCPExit(Activity context) {
        if (!Slave.hasPurchased(context)) {
            if (Slave.CP_is_exit.equals(Slave.CP_YES)) {
                if (Utils.isPackageInstalled(Slave.CP_package_name, context)) {
                    if (Utils.getDaysDiff(context) >= Utils.getStringtoInt(Slave.CP_startday)) {

                        if (Utils.isNetworkConnected(context)) {

                            Intent intent = new Intent(context, FullPagePromo.class);
                            intent.putExtra("type", EngineClient.IH_CP_EXIT);
                            intent.putExtra("src", Slave.CP_camp_img);
                            intent.putExtra("link", Slave.CP_camp_click_link);
                            context.startActivity(intent);
                        }
                    }
                }
            }
        }
    }

    private void handle_launch_prompt(Context context) {
        int rate_nonRepeat, cp_nonRepeat, full_nonRepeat, removeads_nonRepeat;

        if (Slave.LAUNCH_NON_REPEAT_COUNT != null && Slave.LAUNCH_NON_REPEAT_COUNT.size() > 0) {
            for (int i = 0; i < Slave.LAUNCH_NON_REPEAT_COUNT.size(); i++) {
                rate_nonRepeat = Utils.getStringtoInt(Slave.LAUNCH_NON_REPEAT_COUNT.get(i).launch_rate);
                cp_nonRepeat = Utils.getStringtoInt(Slave.LAUNCH_NON_REPEAT_COUNT.get(i).launch_exit);
                full_nonRepeat = Utils.getStringtoInt(Slave.LAUNCH_NON_REPEAT_COUNT.get(i).launch_full);
                removeads_nonRepeat = Utils.getStringtoInt(Slave.LAUNCH_NON_REPEAT_COUNT.get(i).launch_removeads);

                PrintLog.print("handle launch count " + " " + DataHubConstant.APP_LAUNCH_COUNT + " " + rate_nonRepeat + " " + cp_nonRepeat + " " + full_nonRepeat + " " + removeads_nonRepeat);
                if (DataHubConstant.APP_LAUNCH_COUNT == rate_nonRepeat) {
                    PrintLog.print("handle launch prompt inside 1 rate");
                    if (promptHander == null) {
                        promptHander = new PromptHander();
                    }

                    promptHander.rateUsDialog(context);

                    return;
                } else if (DataHubConstant.APP_LAUNCH_COUNT == cp_nonRepeat) {
                    PrintLog.print("handle launch prompt ding check inside 2 cp start");
                    showCPStart((Activity) context);
                    return;
                } else if (DataHubConstant.APP_LAUNCH_COUNT == full_nonRepeat) {
                    PrintLog.print("handle launch prompt inside 3 fullads");
                    //showFullAdsOnLaunch((Activity) context, false, "non repeat");
                    showFullAdsOnLaunch((Activity) context);
                    System.out.println("Admob 0604 Requesting Test  Admob ads goign to call from DB");

                    return;
                } else if (DataHubConstant.APP_LAUNCH_COUNT == removeads_nonRepeat) {
                    PrintLog.print("handle launch prompt inside 4 removeads");
                    showRemoveAdsPrompt(context);
                    return;
                }
            }
        }
        PrintLog.print("handle launch prompt repease" + " " + DataHubConstant.APP_LAUNCH_COUNT + " " + Slave.LAUNCH_REPEAT_FULL_ADS);
        if (Slave.LAUNCH_REPEAT_FULL_ADS != null && !Slave.LAUNCH_REPEAT_FULL_ADS.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.LAUNCH_REPEAT_FULL_ADS) == 0) {
            PrintLog.print("handle launch prompt inside 13 fullads");
            // showFullAdsOnLaunch((Activity) context, false, "repeat");
            showFullAdsOnLaunch((Activity) context);
        } else if (Slave.LAUNCH_REPEAT_EXIT != null && !Slave.LAUNCH_REPEAT_EXIT.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.LAUNCH_REPEAT_EXIT) == 0) {
            PrintLog.print("handle launch prompt inside 12 cp exit");
            showCPStart((Activity) context);
        } else if (Slave.LAUNCH_REPEAT_RATE != null && !Slave.LAUNCH_REPEAT_RATE.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.LAUNCH_REPEAT_RATE) == 0) {
            PrintLog.print("handle launch prompt inside 11 rate");
            if (promptHander == null) {
                promptHander = new PromptHander();
            }

            promptHander.rateUsDialog(context);

        } else if (Slave.LAUNCH_REPEAT_REMOVEADS != null && !Slave.LAUNCH_REPEAT_REMOVEADS.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.LAUNCH_REPEAT_REMOVEADS) == 0) {
            PrintLog.print("handle launch prompt inside 14 removeads");
            showRemoveAdsPrompt(context);
        }
    }

    private void handle_exit_prompt(Context context) {
        int rate_nonRepeat, exit_nonRepeat, full_nonRepeat, removeads_nonRepeat;

        if (Slave.EXIT_NON_REPEAT_COUNT != null && Slave.EXIT_NON_REPEAT_COUNT.size() > 0) {
            for (int i = 0; i < Slave.EXIT_NON_REPEAT_COUNT.size(); i++) {
                rate_nonRepeat = Utils.getStringtoInt(Slave.EXIT_NON_REPEAT_COUNT.get(i).rate);
                exit_nonRepeat = Utils.getStringtoInt(Slave.EXIT_NON_REPEAT_COUNT.get(i).exit);
                full_nonRepeat = Utils.getStringtoInt(Slave.EXIT_NON_REPEAT_COUNT.get(i).full);
                removeads_nonRepeat = Utils.getStringtoInt(Slave.EXIT_NON_REPEAT_COUNT.get(i).removeads);

                PrintLog.print("handle exit" + " " + DataHubConstant.APP_LAUNCH_COUNT + " " + rate_nonRepeat + " " + exit_nonRepeat + " " + full_nonRepeat + " " + removeads_nonRepeat);
                if (DataHubConstant.APP_LAUNCH_COUNT == rate_nonRepeat) {
                    PrintLog.print("handle exit inside 1 rate");
                    if (promptHander == null) {
                        promptHander = new PromptHander();
                    }
                    promptHander.rateUsDialog(context);

                    return;
                } else if (DataHubConstant.APP_LAUNCH_COUNT == exit_nonRepeat) {
                    PrintLog.print("handle exit inside 2 cp exit");
                    showCPExit((Activity) context);
                    return;
                } else if (DataHubConstant.APP_LAUNCH_COUNT == full_nonRepeat) {
                    PrintLog.print("handle exit inside 3 fullads");
                    if (!Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT.equals("true")) {
                        showFullAdsOnExit((Activity) context);
                    }
                    return;
                } else if (DataHubConstant.APP_LAUNCH_COUNT == removeads_nonRepeat) {
                    PrintLog.print("handle exit inside 4 removeads");
                    showRemoveAdsPrompt(context);
                    return;
                }
            }
        }
        PrintLog.print("handle exit repeat check" + " " + DataHubConstant.APP_LAUNCH_COUNT + " " + Slave.EXIT_REPEAT_FULL_ADS);
        if (Slave.EXIT_REPEAT_FULL_ADS != null && !Slave.EXIT_REPEAT_FULL_ADS.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.EXIT_REPEAT_FULL_ADS) == 0) {
            PrintLog.print("handle exit inside 13 fullads" + " " + Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT);
//            showFullAdsOnExit((Activity) context, false, "repeat");
            if (Slave.EXIT_SHOW_AD_ON_EXIT_PROMPT.equals("false")) {
                showFullAdsOnExit((Activity) context);
            }
        } else if (Slave.EXIT_REPEAT_EXIT != null && !Slave.EXIT_REPEAT_EXIT.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.EXIT_REPEAT_EXIT) == 0) {
            PrintLog.print("handle exit inside 12 cp exit");
            showCPExit((Activity) context);
        } else if (Slave.EXIT_REPEAT_RATE != null && !Slave.EXIT_REPEAT_RATE.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.EXIT_REPEAT_RATE) == 0) {
            PrintLog.print("handle exit inside 11 rate");
            if (promptHander == null) {
                promptHander = new PromptHander();
            }

            promptHander.rateUsDialog(context);

        } else if (Slave.EXIT_REPEAT_REMOVEADS != null && !Slave.EXIT_REPEAT_REMOVEADS.equalsIgnoreCase("") && DataHubConstant.APP_LAUNCH_COUNT % Utils.getStringtoInt(Slave.EXIT_REPEAT_REMOVEADS) == 0) {
            PrintLog.print("handle exit inside 14 removeads");
            showRemoveAdsPrompt(context);
        }
    }

    /**
     * @param context mapper class handeling
     */
    private void callingForMapper(Activity context) {
        Intent intent = context.getIntent();
        String type = intent.getStringExtra(MapperUtils.keyType);
        String value = intent.getStringExtra(MapperUtils.keyValue);
        System.out.println("AHandler.callingForMapper " + type + " " + value);
        try {
            if (type != null && value != null) {
                if (type.equalsIgnoreCase("url")) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    builder.addDefaultShareMenuItem();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(value));

                } else if (type.equalsIgnoreCase("deeplink")) {
                    switch (value) {

                        case MapperUtils.gcmMoreApp:
                            //Remember to add here your gcmMoreApp.
                            new Utils().moreApps(context);

                            break;
                        case MapperUtils.gcmRateApp:
                            //Remember to add here your RareAppClass.
                            new PromptHander().rateUsDialog(context);

                            break;
                        case MapperUtils.gcmRemoveAds:
                            //Remember to add here your RemoveAdClass.
                            showRemoveAdsPrompt(context);
                            break;
                        case MapperUtils.gcmFeedbackApp:
                            new Utils().showFeedbackPrompt(context, "Please share your valuable feedback.");
                            break;
                        case MapperUtils.gcmShareApp:
                            //Remember to add here your ShareAppClass.
                            new Utils().showSharePrompt(context, "Share this cool & fast performance app with friends & family");
                            break;
                        case MapperUtils.gcmForceAppUpdate:
                            new Utils().showAppUpdatePrompt(context);
                            break;


                    }
                }
            }
        } catch (Exception e) {
            System.out.println("AHandler.callingForMapper excep " + e.getMessage());
        }
    }

    public ArrayList<MoreFeature> getMoreFeatures() {
        return MoreFeatureResponseHandler.getInstance().getMoreFeaturesListResponse();
    }

    public void onAHandlerDestroy() {
        AdsHelper.getInstance().onAHandlerDestroy();
    }


}

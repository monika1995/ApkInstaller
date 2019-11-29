package app.serviceprovider;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import app.enginev4.AdsEnum;
import app.listener.AppAdsListener;
import app.listener.AppFullAdsListener;
import app.pnd.adshandler.BuildConfig;
import app.pnd.adshandler.R;

public class AdMobAds {
    static final String DEVICE_ID_1 = "CE0B0B18784B2BC37B7DF29D250342AD"; //Office samsung j2
    static final String DEVICE_ID_2 = "FBEE963E8B04CC4026D543BFFC1F3479"; //Office OPPO api 23
    static final String DEVICE_ID_3 = "AFBAAC019C9D3C98744A01B540B4D874"; //Office Xiaomi Redmi Note 3
    static final String DEVICE_ID_4 = "2A25AD8F00457CE180B03544F1941575"; //Office Samsung G6
    static final String DEVICE_ID_5 = "0BB00B090AB5C7707998CC95A4E84841"; //Office Samsung j2 api 27
    static final String DEVICE_ID_6 = "6040D60587FCD92DB8EF5219AFB04C37"; //Office OPPO A3 api22
    static final String DEVICE_ID_7 = "81B319DB2DCF85A0FEA17FD6BDDC06F7"; //Office Samsung G5
    static final String DEVICE_ID_8 = "6C375A950789902A01F70F395CA69522"; //Office Xiaomi Redmi Note 7
    static final String DEVICE_ID_9 = "1A884CBAA9A6A16984C6290B52FC1A03"; //Huawei PRITI
    static final String DEVICE_ID_10 = "D9281CD323A5304F4F1D1D44E26C81ED";//OFC J2********


    private static AdMobAds instance;
    private InterstitialAd mInterstitial;
    //private AdView mAdView;

    private AdMobAds(Context context) {
        MobileAds.initialize(context, context.getResources().getString(R.string.app_id_admob));
        if (mInterstitial == null)
            mInterstitial = new InterstitialAd(context);
    }


    public static AdMobAds getAdmobOBJ(Context context) {
        if (instance == null) {
            synchronized (AdMobAds.class) {
                if (instance == null) {
                    instance = new AdMobAds(context);
                }
            }
        }
        return instance;
    }

    //working
    public void admob_GetBannerAds(Context context, String id, final AppAdsListener listener) {
        if (id != null && !id.equals("")) {
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/6300978111".trim();
            }
            id = id.trim();
            System.out.println("AdMobAds.admob_GetBannerAds " + id);

            AdView adView = new AdView(context);
            adView.setAdUnitId(id);

            adView.setAdSize(AdSize.BANNER);

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(DEVICE_ID_1)
                    .addTestDevice(DEVICE_ID_2)
                    .addTestDevice(DEVICE_ID_3)
                    .addTestDevice(DEVICE_ID_4)
                    .addTestDevice(DEVICE_ID_5)
                    .addTestDevice(DEVICE_ID_6)
                    .addTestDevice(DEVICE_ID_7)
                    .addTestDevice(DEVICE_ID_8)
                    .addTestDevice(DEVICE_ID_9)
                    .addTestDevice(DEVICE_ID_10)
                    .build();

            try {
                adView.setAdListener(new AdMobAdsListener(adView, listener));
                adView.loadAd(adRequest);
            } catch (Exception e) {
                listener.onAdFailed(AdsEnum.ADS_ADMOB, e.getMessage());
                e.printStackTrace();
            }

        } else {
            listener.onAdFailed(AdsEnum.ADS_ADMOB, "Banner Id null");
        }
    }

    //working
    public void admob_GetBannerRectangleAds(Context context, String id, final AppAdsListener listener) {
        if (id != null && !id.equals("")) {
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/6300978111".trim();
            }
            id = id.trim();
            System.out.println("AdMobAds.admob_GetBannerRectangleAds " + id);

            AdView adView = new AdView(context);
            adView.setAdUnitId(id);

            adView.setAdSize(AdSize.MEDIUM_RECTANGLE);

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(DEVICE_ID_1)
                    .addTestDevice(DEVICE_ID_2)
                    .addTestDevice(DEVICE_ID_3)
                    .addTestDevice(DEVICE_ID_4)
                    .addTestDevice(DEVICE_ID_5)
                    .addTestDevice(DEVICE_ID_6)
                    .addTestDevice(DEVICE_ID_7)
                    .addTestDevice(DEVICE_ID_8)
                    .addTestDevice(DEVICE_ID_9)
                    .addTestDevice(DEVICE_ID_10)
                    .build();

            try {
                adView.setAdListener(new AdMobAdsListener(adView, listener));
                adView.loadAd(adRequest);
            } catch (Exception e) {
                listener.onAdFailed(AdsEnum.ADS_ADMOB, e.getMessage());
                e.printStackTrace();
            }

        } else {
            listener.onAdFailed(AdsEnum.ADS_ADMOB, "Banner Rectangle Id null");
        }

    }

    //working
    public void admob_GetBannerLargeAds(Context context, String id, final AppAdsListener listener) {
        if (id != null && !id.equals("")) {
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/6300978111".trim();
            }
            id = id.trim();
            System.out.println("AdMobAds.admob_GetBannerRectangleAds " + id);

            AdView adView = new AdView(context);
            adView.setAdUnitId(id);

            adView.setAdSize(AdSize.LARGE_BANNER);

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(DEVICE_ID_1)
                    .addTestDevice(DEVICE_ID_2)
                    .addTestDevice(DEVICE_ID_3)
                    .addTestDevice(DEVICE_ID_4)
                    .addTestDevice(DEVICE_ID_5)
                    .addTestDevice(DEVICE_ID_6)
                    .addTestDevice(DEVICE_ID_7)
                    .addTestDevice(DEVICE_ID_8)
                    .addTestDevice(DEVICE_ID_9)
                    .addTestDevice(DEVICE_ID_10)
                    .build();

            try {
                adView.setAdListener(new AdMobAdsListener(adView, listener));
                adView.loadAd(adRequest);
            } catch (Exception e) {
                listener.onAdFailed(AdsEnum.ADS_ADMOB, e.getMessage());
                e.printStackTrace();
            }

        } else {
            listener.onAdFailed(AdsEnum.ADS_ADMOB, "Banner Large Id null");
        }
    }

    /**
     * admob_InitFullAds function call only splash for first time caching ads and after ads onAdClosed
     */
    public void admob_InitFullAds(final Context context, String id, final AppFullAdsListener listener, final boolean isFromCache) {
        if (id != null && !id.equals("")) {
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/1033173712".trim();
            }
          //  Log.d("AdMobAds  ", "Admob 0604 Requesting Test admob_InitFullAds full ads id "+id);
            System.out.println("Admob 0604 Requesting Test admob_InitFullAds full ads id  "+id);

            id = id.trim();
            mInterstitial = new InterstitialAd(context);
            mInterstitial.setAdUnitId(id);
            final String finalId = id;
            mInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    if (isFromCache)
                        listener.onFullAdLoaded();
                   // Log.d("AdMobAds  ", "Admob 0604 Requesting Test  Admob ads loaded Sucesssss ");
                    System.out.println("Admob 0604 Requesting Test  Admob ads loaded Sucesssss");

                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    if (isFromCache)
                        listener.onFullAdFailed(AdsEnum.FULL_ADS_ADMOB, String.valueOf(errorCode));
                    System.out.println("Admob 0604 Requesting Test  Admob ads failedd with eror code "+errorCode);
                }

                @Override
                public void onAdOpened() {

                }

                @Override
                public void onAdLeftApplication() {

                }

                @Override
                public void onAdClosed() {
                    admob_InitFullAds(context, finalId, listener, false);
                }
            });

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(DEVICE_ID_1)
                    .addTestDevice(DEVICE_ID_2)
                    .addTestDevice(DEVICE_ID_3)
                    .addTestDevice(DEVICE_ID_4)
                    .addTestDevice(DEVICE_ID_5)
                    .addTestDevice(DEVICE_ID_6)
                    .addTestDevice(DEVICE_ID_7)
                    .addTestDevice(DEVICE_ID_8)
                    .addTestDevice(DEVICE_ID_9)
                    .addTestDevice(DEVICE_ID_10)
                    .build();

            try {
                mInterstitial.loadAd(adRequest);
            } catch (Exception e) {
                listener.onFullAdFailed(AdsEnum.FULL_ADS_ADMOB, e.getMessage());
            }

        } else {
            listener.onFullAdFailed(AdsEnum.FULL_ADS_ADMOB, "Init FullAds Id null");
        }

    }

    public void admob_showFullAds(Context context, String id, AppFullAdsListener listener) {
        System.out.println("Admob 0604 Requesting Test  Admob going to show ads with "+id +" mInterstitial is "+mInterstitial + " and ads loaded status is "+mInterstitial.isLoaded() );
        if (context != null && id != null && !id.equals("")) {
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/1033173712".trim();
            }
            id = id.trim();
            Log.d("AdMobAds", "Meenu admob_showFullAds " + id);
            if (mInterstitial != null) {
                if (mInterstitial.isLoaded()) {
                    try {
                        mInterstitial.show();
                        listener.onFullAdLoaded();
                    } catch (Exception e) {
                        listener.onFullAdFailed(AdsEnum.FULL_ADS_ADMOB, e.getMessage());
                    }

                } else {
                    System.out.println("AdMobAds.admob_showFullAds " + mInterstitial.isLoaded());
                    admob_InitFullAds(context, id, listener, false);
                    listener.onFullAdFailed(AdsEnum.FULL_ADS_ADMOB, String.valueOf(mInterstitial.isLoaded()));
                }
            } else {
                listener.onFullAdFailed(AdsEnum.FULL_ADS_ADMOB, "Admob Interstitial null");
            }
        } else {
            listener.onFullAdFailed(AdsEnum.FULL_ADS_ADMOB, "FullAds Id null");
        }
    }

}

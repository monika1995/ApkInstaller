package app.serviceprovider;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import app.enginev4.AdsEnum;
import app.listener.AppAdsListener;
import app.pnd.adshandler.BuildConfig;

import static app.serviceprovider.AdMobAds.DEVICE_ID_1;
import static app.serviceprovider.AdMobAds.DEVICE_ID_10;
import static app.serviceprovider.AdMobAds.DEVICE_ID_2;
import static app.serviceprovider.AdMobAds.DEVICE_ID_3;
import static app.serviceprovider.AdMobAds.DEVICE_ID_4;
import static app.serviceprovider.AdMobAds.DEVICE_ID_5;
import static app.serviceprovider.AdMobAds.DEVICE_ID_6;
import static app.serviceprovider.AdMobAds.DEVICE_ID_7;
import static app.serviceprovider.AdMobAds.DEVICE_ID_8;
import static app.serviceprovider.AdMobAds.DEVICE_ID_9;

/**
 * Created by Meenu Singh on 2019-08-26.
 */
public class AdMobAdaptive {
    private static AdMobAdaptive instance;

    private AdMobAdaptive(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    public static AdMobAdaptive getAdmobAdaptiveObj(Context context) {
        if (instance == null) {
            synchronized (AdMobAdaptive.class) {
                if (instance == null) {
                    instance = new AdMobAdaptive(context);
                }
            }
        }
        return instance;
    }

    public void admob_GetBannerAdaptive(Activity context, String id, final AppAdsListener listener) {
        if (id != null && !id.equals("")) {
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/6300978111".trim();
            }
            AdView adView = new AdView(context);
            adView.setAdUnitId(id);
            AdSize adSize = getAdSize(context);
            adView.setAdSize(adSize);

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

    private AdSize getAdSize(Activity activity) {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationBannerAdSizeWithWidth(activity, adWidth);
    }
}

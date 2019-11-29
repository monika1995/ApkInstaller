package app.serviceprovider;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applovin.adview.AppLovinAdView;
import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.nativeAds.AppLovinNativeAd;
import com.applovin.nativeAds.AppLovinNativeAdLoadListener;
import com.applovin.nativeAds.AppLovinNativeAdPrecacheListener;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdClickListener;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkUtils;

import java.util.List;

import app.enginev4.AdsEnum;
import app.listener.AppAdsListener;
import app.listener.AppFullAdsListener;
import app.pnd.adshandler.R;
import app.utils.applovinadshelper.AppLovinCarouselViewSettings;
import app.utils.applovinadshelper.InlineCarouselCardMediaView;
import app.utils.applovinadshelper.InlineCarouselCardState;

import static com.applovin.sdk.AppLovinSdkUtils.runOnUiThread;


/**
 * Created by Meenu Singh on 19/06/19.
 */
public class AppLovinAdsProvider {
    private static AppLovinAdsProvider appLovinAdsProvider = null;

    private AppLovinAd currentAd;
    private AppLovinInterstitialAdDialog interstitialAd;
    private AppLovinSdk appLovinSdk;
    private AppLovinNativeAd nativeAd;


    private AppLovinAdsProvider(Context context) {
        appLovinSdk = AppLovinSdk.getInstance(context);
        interstitialAd = AppLovinInterstitialAd.create(appLovinSdk, context);
    }

    public static AppLovinAdsProvider getAppLovinObject(Context context) {
        if (appLovinAdsProvider == null) {
            synchronized (AppLovinAdsProvider.class) {
                if (appLovinAdsProvider == null) {
                    appLovinAdsProvider = new AppLovinAdsProvider(context);
                }
            }
        }
        return appLovinAdsProvider;
    }

    public void getAppLovinBanner(Context context, final AppAdsListener listener) {
        final AppLovinAdView adView = new AppLovinAdView(AppLovinAdSize.BANNER, context);
        adView.setGravity(RelativeLayout.CENTER_IN_PARENT);
        try {
            adView.setAdLoadListener(new AppLovinAdsListener(adView, listener));
        } catch (Exception e) {
            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, e.getMessage());
            e.printStackTrace();
        }
        adView.loadNextAd();
    }

    public void getAppLovinBannerLarge(Context context, final AppAdsListener listener) {
        final AppLovinAdView adView = new AppLovinAdView(AppLovinAdSize.LEADER, context);
        adView.setGravity(RelativeLayout.CENTER_IN_PARENT);

        try {
            adView.setAdLoadListener(new AppLovinAdsListener(adView, listener));
        } catch (Exception e) {
            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, e.getMessage());
            e.printStackTrace();
        }
        adView.loadNextAd();
    }

    public void getAppLovinBannerRectangle(Context context, final AppAdsListener listener) {
        final AppLovinAdView adView = new AppLovinAdView(AppLovinAdSize.MREC, context);
        adView.setGravity(RelativeLayout.CENTER_IN_PARENT);

        try {
            adView.setAdLoadListener(new AppLovinAdsListener(adView, listener));
        } catch (Exception e) {
            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, e.getMessage());
            e.printStackTrace();
        }
        adView.loadNextAd();
    }

    public void showAppLovinNativeMedium(final Context context, final AppAdsListener listener) {
        appLovinSdk.getNativeAdService().loadNextAd(new AppLovinNativeAdLoadListener() {
            @Override
            public void onNativeAdsLoaded(final List<AppLovinNativeAd> nativeAds) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            nativeAd = nativeAds.get(0);
                            preFectchNativeAds();
                            LinearLayout linearLayout = new LinearLayout(context);
                            linearLayout.addView(getNativeMediumAds(context));
                            listener.onAdLoaded(linearLayout);
                        } catch (Exception e) {
                            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, e.getMessage());
                        }
                    }
                });

            }

            @Override
            public void onNativeAdsFailedToLoad(int errorCode) {
                listener.onAdFailed(AdsEnum.ADS_APPLOVIN, String.valueOf(errorCode));
            }
        });

    }

    public void showAppLovinNativeLarge(final Context context, final AppAdsListener listener) {
        appLovinSdk.getNativeAdService().loadNextAd(new AppLovinNativeAdLoadListener() {
            @Override
            public void onNativeAdsLoaded(final List<AppLovinNativeAd> nativeAds) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            nativeAd = nativeAds.get(0);
                            preFectchNativeAds();
                            LinearLayout linearLayout = new LinearLayout(context);
                            linearLayout.addView(getNativelargeAds(context));
                            listener.onAdLoaded(linearLayout);
                        } catch (Exception e) {
                            listener.onAdFailed(AdsEnum.ADS_APPLOVIN, e.getMessage());
                        }
                    }
                });

            }

            @Override
            public void onNativeAdsFailedToLoad(int errorCode) {
                listener.onAdFailed(AdsEnum.ADS_APPLOVIN, String.valueOf(errorCode));
            }
        });

    }


    private void preFectchNativeAds() {
        appLovinSdk.getNativeAdService().precacheResources(nativeAd, new AppLovinNativeAdPrecacheListener() {
            @Override
            public void onNativeAdImagesPrecached(AppLovinNativeAd appLovinNativeAd) {


            }

            @Override
            public void onNativeAdVideoPreceached(AppLovinNativeAd appLovinNativeAd) {
                // This will get called whether an ad actually has a video to precache or not

            }

            @Override
            public void onNativeAdImagePrecachingFailed(AppLovinNativeAd appLovinNativeAd, int i) {

            }

            @Override
            public void onNativeAdVideoPrecachingFailed(AppLovinNativeAd appLovinNativeAd, int i) {

            }
        });
    }

    private View getNativelargeAds(final Context context) {
        LinearLayout adsviewLayout = new LinearLayout(context);
        InlineCarouselCardMediaView mediaView = new InlineCarouselCardMediaView(context);
        mediaView.setAd(nativeAd);
        mediaView.setCardState(new InlineCarouselCardState());
        mediaView.setSdk(appLovinSdk);
        mediaView.setUiHandler(new Handler(Looper.getMainLooper()));
        mediaView.setUpView();
        mediaView.autoplayVideo();
        RelativeLayout adView = (RelativeLayout) ((Activity) context).
                getLayoutInflater().inflate(R.layout.ad_applovin_native_large,
                adsviewLayout, false);

        populateAdsViews(context, adView, mediaView);
        adsviewLayout.addView(adView);
        return adsviewLayout;
    }

    private View getNativeMediumAds(final Context context) {
        LinearLayout adsviewLayout = new LinearLayout(context);
        InlineCarouselCardMediaView mediaView = new InlineCarouselCardMediaView(context);
        mediaView.setAd(nativeAd);
        mediaView.setCardState(new InlineCarouselCardState());
        mediaView.setSdk(appLovinSdk);
        mediaView.setUiHandler(new Handler(Looper.getMainLooper()));
        mediaView.setUpView();
        mediaView.autoplayVideo();


        LinearLayout adView = (LinearLayout) ((Activity) context).
                getLayoutInflater().inflate(R.layout.ad_applovin_native_medium,
                adsviewLayout, false);

        populateAdsViews(context, adView, mediaView);

        adsviewLayout.addView(adView);
        return adsviewLayout;
    }

    private void populateAdsViews(final Context context, ViewGroup adView, final InlineCarouselCardMediaView mediaView) {
        final AppLovinNativeAd nativeAd = mediaView.getAd();
        TextView appTitle = adView.findViewById(R.id.appTitleTextView);
        TextView appDesc = adView.findViewById(R.id.appDescriptionTextView);
        ImageView appIcon = adView.findViewById(R.id.appIcon);
        Button appButton = adView.findViewById(R.id.appDownloadButton);
        FrameLayout frameLayout = adView.findViewById(R.id.mediaViewPlaceholder);

        AppLovinSdkUtils.safePopulateImageView(appIcon, Uri.parse(nativeAd.getIconUrl()), AppLovinSdkUtils.dpToPx(context, AppLovinCarouselViewSettings.ICON_IMAGE_MAX_SCALE_SIZE));

        frameLayout.addView(mediaView);

        appTitle.setText(nativeAd.getTitle());
        appDesc.setText(nativeAd.getDescriptionText());
        appButton.setText(nativeAd.getCtaText());
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nativeAd.launchClickTarget(context);
            }
        });


    }


    public void loadAppLovinFullAds(final Context context, final AppFullAdsListener listener, final boolean isFromCache) {
        if (interstitialAd == null) {
            interstitialAd = AppLovinInterstitialAd.create(appLovinSdk, context);
        }
        appLovinSdk.getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
            @Override
            public void adReceived(AppLovinAd ad) {
                try {
                    currentAd = ad;
                    if (isFromCache)
                        listener.onFullAdLoaded();
                    System.out.println("AppLovinAdsProvider.adReceived");
                } catch (Exception e) {
                    listener.onFullAdFailed(AdsEnum.FULL_ADS_APPLOVIN, e.getMessage());
                }


            }

            @Override
            public void failedToReceiveAd(int errorCode) {
                // Look at AppLovinErrorCodes.java for list of error codes
                System.out.println("AppLovinAdsProvider.failedToReceiveAd");
                if (isFromCache)
                    listener.onFullAdFailed(AdsEnum.FULL_ADS_APPLOVIN, String.valueOf(errorCode));
            }
        });
    }

    public void showAppLovinFullAds(final Context context, final AppFullAdsListener listener) {
        if (currentAd != null || interstitialAd.isAdReadyToDisplay()) {
            try {
                interstitialAd.showAndRender(currentAd);
                listener.onFullAdLoaded();
                System.out.println("AppLovinAdsProvider.showAppLovinFullAds");
            } catch (Exception e) {
                listener.onFullAdFailed(AdsEnum.FULL_ADS_APPLOVIN, e.getMessage());
            }

        } else {
            loadAppLovinFullAds(context, listener, false);
            listener.onFullAdFailed(AdsEnum.FULL_ADS_APPLOVIN, String.valueOf(interstitialAd.isAdReadyToDisplay()));
            System.out.println("AppLovinAdsProvider.showAppLovinFullAds failed");

        }

        //
        // Optional: Set ad display, ad click, and ad video playback callback listeners
        //
        interstitialAd.setAdDisplayListener(new AppLovinAdDisplayListener() {
            @Override
            public void adDisplayed(AppLovinAd appLovinAd) {
                System.out.println("AppLovinAdsProvider.adDisplayed");
            }

            @Override
            public void adHidden(AppLovinAd appLovinAd) {
                System.out.println("AppLovinAdsProvider.adHidden");
                loadAppLovinFullAds(context, listener, false);
            }
        });

        interstitialAd.setAdClickListener(new AppLovinAdClickListener() {
            @Override
            public void adClicked(AppLovinAd appLovinAd) {
                System.out.println("AppLovinAdsProvider.adClicked");
            }
        });
        // This will only ever be used if you have video ads enabled.
        interstitialAd.setAdVideoPlaybackListener(new AppLovinAdVideoPlaybackListener() {
            @Override
            public void videoPlaybackBegan(AppLovinAd appLovinAd) {
            }

            @Override
            public void videoPlaybackEnded(AppLovinAd appLovinAd, double percentViewed, boolean wasFullyViewed) {
                System.out.println("AppLovinAdsProvider.videoPlaybackEnded " + wasFullyViewed);
            }
        });
    }

}

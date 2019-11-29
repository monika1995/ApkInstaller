package app.serviceprovider;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.List;

import app.enginev4.AdsEnum;
import app.listener.AppAdsListener;
import app.pnd.adshandler.BuildConfig;
import app.pnd.adshandler.R;


/**
 * Created by Meenu Singh on 11/06/19.
 */

public class AdmobNativeAdvanced {
    private UnifiedNativeAd mainUnifiedNativeAd;
    private static AdmobNativeAdvanced instance;

    private AdmobNativeAdvanced(Context context) {
        MobileAds.initialize(context, context.getResources().getString(R.string.app_id_admob));

    }

    public static AdmobNativeAdvanced getInstance(Context context) {
        if (instance == null) {
            synchronized (AdmobNativeAdvanced.class) {
                if (instance == null) {
                    instance = new AdmobNativeAdvanced(context);
                }
            }
        }
        return instance;
    }

    private void populateUnifiedNativeAdForLarge(UnifiedNativeAd unifiedNativeAd,
                                                 UnifiedNativeAdView adView) {
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = unifiedNativeAd.getVideoController();

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                // Publishers should allow native ads to complete video playback before refreshing
                // or replacing them with another ad in the same UI location.
                super.onVideoEnd();
            }
        });

        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());

        if (unifiedNativeAd.getIcon() != null) {
            adView.getIconView().setVisibility(View.VISIBLE);
            ((ImageView) adView.getIconView()).setImageDrawable(
                    unifiedNativeAd.getIcon().getDrawable());
        } else {
            adView.getIconView().setVisibility(View.GONE);
        }

        MediaView mediaView = adView.findViewById(R.id.appinstall_media);
        ImageView mainImageView = adView.findViewById(R.id.appinstall_image);


        // Apps can check the VideoController's hasVideoContent property to determine if the
        // NativeAppInstallAd has a video asset.
        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mediaView.setVisibility(View.VISIBLE);
            mainImageView.setVisibility(View.GONE);

        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);
            mainImageView.setVisibility(View.VISIBLE);

            // At least one image is guaranteed.
            List<NativeAd.Image> images = unifiedNativeAd.getImages();
            if (images != null && images.size() > 0) {
                mainImageView.setImageDrawable(images.get(0).getDrawable());
            }
        }

        // These assets aren't guaranteed to be in every NativeAppInstallAd, so it's important to
        // check before trying to display them.
        if (unifiedNativeAd.getPrice() != null) {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
        } else {
            adView.getPriceView().setVisibility(View.GONE);
        }

        if (unifiedNativeAd.getStore() != null) {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
        } else {
            adView.getStoreView().setVisibility(View.GONE);
        }

        if (unifiedNativeAd.getStarRating() != null) {
            adView.getStarRatingView().setVisibility(View.VISIBLE);
            ((RatingBar) adView.getStarRatingView()).setRating(unifiedNativeAd.getStarRating().floatValue());
        } else {
            adView.getStarRatingView().setVisibility(View.GONE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(unifiedNativeAd);
    }

    /**
     * @param nativeContentAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateUnifiedNativeAdForMedium(UnifiedNativeAd nativeContentAd,
                                                  UnifiedNativeAdView adView) {

        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setImageView(adView.findViewById(R.id.contentad_image));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getIcon();

        if (logoImage == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(logoImage.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     */
    private void getNativeAdvancedAds(final Activity context, String id,
                                      final boolean isNativeLarge, final AppAdsListener listener) {
        if (id != null && !id.equals("")) {
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/2247696110".trim();
            }
            id = id.trim();
            AdLoader.Builder builder = new AdLoader.Builder(context, id);
            if (isNativeLarge) {
                builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        mainUnifiedNativeAd = unifiedNativeAd;
                        final LinearLayout linearLayout = new LinearLayout(context);
                        UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater().inflate(R.layout.ad_admob_native_large, linearLayout, false);
                        populateUnifiedNativeAdForLarge(unifiedNativeAd, adView);
                        linearLayout.addView(adView);
                        if (listener != null)
                            listener.onAdLoaded(linearLayout);
                    }
                });
            } else {
                builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        mainUnifiedNativeAd = unifiedNativeAd;
                        final LinearLayout linearLayout = new LinearLayout(context);
                        UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater().inflate(R.layout.ad_admob_native_medium, linearLayout, false);
                        populateUnifiedNativeAdForMedium(unifiedNativeAd, adView);
                        linearLayout.addView(adView);
                        if (listener != null)
                            listener.onAdLoaded(linearLayout);
                    }
                });
            }

            VideoOptions videoOptions = new VideoOptions.Builder()
                    .setStartMuted(false)
                    .build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    System.out.println("AdmobNativeAdvanced.onAdFailedToLoad getNativeAdvancedAds " + errorCode + " " + isNativeLarge);
                    if (listener != null)
                        listener.onAdFailed(AdsEnum.ADS_ADMOB, String.valueOf(errorCode));
                }
            }).build();

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(AdMobAds.DEVICE_ID_1)
                    .addTestDevice(AdMobAds.DEVICE_ID_2)
                    .addTestDevice(AdMobAds.DEVICE_ID_3)
                    .addTestDevice(AdMobAds.DEVICE_ID_4)
                    .addTestDevice(AdMobAds.DEVICE_ID_5)
                    .addTestDevice(AdMobAds.DEVICE_ID_6)
                    .addTestDevice(AdMobAds.DEVICE_ID_7)
                    .addTestDevice(AdMobAds.DEVICE_ID_8)
                    .addTestDevice(AdMobAds.DEVICE_ID_9)
                    .addTestDevice(AdMobAds.DEVICE_ID_10)
                    .build();

            try {
                adLoader.loadAd(adRequest);
            } catch (Exception e) {
                if (listener != null)
                    listener.onAdFailed(AdsEnum.ADS_ADMOB, e.getMessage());
            }

        } else {
            if (listener != null)
                listener.onAdFailed(AdsEnum.ADS_ADMOB, "NativeAdvancedAds Id null");
        }

    }


    /**
     * for native ads grid view
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd }.
     *
     * @param unifiedNativeAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateUnifiedNativeAdForGrid(UnifiedNativeAd unifiedNativeAd,
                                                UnifiedNativeAdView adView) {
//        mVideoStatus.setText("Video status: Ad does not contain a video asset.");
//        mRefresh.setEnabled(true);

        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setImageView(adView.findViewById(R.id.contentad_image));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every unifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());

        List<NativeAd.Image> images = unifiedNativeAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = unifiedNativeAd.getIcon();

        if (logoImage != null) {
            ((ImageView) adView.getIconView()).setImageDrawable(logoImage.getDrawable());
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            adView.getIconView().setVisibility(View.INVISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(unifiedNativeAd);
    }


    private void getNativeAdvancedAds_GridView_Ads(final Activity context, String id,
                                                   final ViewGroup moreLayout, final AppAdsListener listener) {
        if (id != null && !id.equals("")) {
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/2247696110".trim();
            }
            id = id.trim();
            AdLoader.Builder builder = new AdLoader.Builder(context, id);
            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    mainUnifiedNativeAd = unifiedNativeAd;
                    final LinearLayout linearLayout = new LinearLayout(context);
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater().inflate(R.layout.ad_admob_grid, linearLayout, false);
                    populateUnifiedNativeAdForGrid(unifiedNativeAd, adView);
                    linearLayout.addView(adView);
                    if (listener != null)
                        listener.onAdLoaded(linearLayout);
                    if (moreLayout != null) {
                        moreLayout.setVisibility(View.GONE);

                    }
                }
            });

            VideoOptions videoOptions = new VideoOptions.Builder()
                    .setStartMuted(false)
                    .build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    System.out.println("AdmobNativeAdvanced.onAdFailedToLoad getNativeAdvancedAds_GridView_Ads" + errorCode);
                    if (listener != null)
                        listener.onAdFailed(AdsEnum.ADS_ADMOB, String.valueOf(errorCode));
                }
            }).build();

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(AdMobAds.DEVICE_ID_1)
                    .addTestDevice(AdMobAds.DEVICE_ID_2)
                    .addTestDevice(AdMobAds.DEVICE_ID_3)
                    .addTestDevice(AdMobAds.DEVICE_ID_4)
                    .addTestDevice(AdMobAds.DEVICE_ID_5)
                    .addTestDevice(AdMobAds.DEVICE_ID_6)
                    .addTestDevice(AdMobAds.DEVICE_ID_7)
                    .addTestDevice(AdMobAds.DEVICE_ID_8)
                    .addTestDevice(AdMobAds.DEVICE_ID_9)
                    .addTestDevice(AdMobAds.DEVICE_ID_10)
                    .build();

            try {
                adLoader.loadAd(adRequest);
            } catch (Exception e) {
                if (listener != null)
                    listener.onAdFailed(AdsEnum.ADS_ADMOB, e.getMessage());
            }

        } else {
            if (listener != null)
                listener.onAdFailed(AdsEnum.ADS_ADMOB, "NativeAdvancedAds_GridView_Ads Id null");
        }

    }


    private void populateUnifiedNativeAdForRect(UnifiedNativeAd unifiedNativeAd,
                                                UnifiedNativeAdView adView) {

        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());

        if (unifiedNativeAd.getIcon() != null) {
            adView.getIconView().setVisibility(View.VISIBLE);
            ((ImageView) adView.getIconView()).setImageDrawable(
                    unifiedNativeAd.getIcon().getDrawable());
        } else {
            adView.getIconView().setVisibility(View.GONE);
        }

        ImageView mainImageView = adView.findViewById(R.id.appinstall_image);


        adView.setImageView(mainImageView);
        mainImageView.setVisibility(View.VISIBLE);

        // At least one image is guaranteed.
        List<NativeAd.Image> images = unifiedNativeAd.getImages();
        if (images != null && images.size() > 0) {
            mainImageView.setImageDrawable(images.get(0).getDrawable());
        }


        // These assets aren't guaranteed to be in every NativeAppInstallAd, so it's important to
        // check before trying to display them.
        if (unifiedNativeAd.getPrice() != null) {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
        } else {
            adView.getPriceView().setVisibility(View.GONE);
        }

        if (unifiedNativeAd.getStore() != null) {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
        } else {
            adView.getStoreView().setVisibility(View.GONE);
        }

        if (unifiedNativeAd.getStarRating() != null) {
            adView.getStarRatingView().setVisibility(View.VISIBLE);
            ((RatingBar) adView.getStarRatingView()).setRating(unifiedNativeAd.getStarRating().floatValue());
        } else {
            adView.getStarRatingView().setVisibility(View.GONE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(unifiedNativeAd);
    }


    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     */
    private void getNativeRectangleAds(final Activity context, String id, final AppAdsListener listener) {
        if (id != null && !id.equals("")) {
            if (BuildConfig.DEBUG) {
                id = "ca-app-pub-3940256099942544/2247696110".trim();
            }
            id = id.trim();
            AdLoader.Builder builder = new AdLoader.Builder(context, id);

            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    mainUnifiedNativeAd = unifiedNativeAd;
                    final LinearLayout linearLayout = new LinearLayout(context);
                    UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater().inflate(R.layout.ad_admob_native_rectangle, linearLayout, false);
                    populateUnifiedNativeAdForRect(unifiedNativeAd, adView);
                    linearLayout.addView(adView);
                    if (listener != null)
                        listener.onAdLoaded(linearLayout);
                }
            });


            VideoOptions videoOptions = new VideoOptions.Builder()
                    .setStartMuted(false)
                    .build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    System.out.println("AdmobNativeAdvanced.onAdFailedToLoad getNativeRectangleAds " + errorCode);
                    if (listener != null)
                        listener.onAdFailed(AdsEnum.ADS_ADMOB, String.valueOf(errorCode));
                }
            }).build();

            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(AdMobAds.DEVICE_ID_1)
                    .addTestDevice(AdMobAds.DEVICE_ID_2)
                    .addTestDevice(AdMobAds.DEVICE_ID_3)
                    .addTestDevice(AdMobAds.DEVICE_ID_4)
                    .addTestDevice(AdMobAds.DEVICE_ID_5)
                    .addTestDevice(AdMobAds.DEVICE_ID_6)
                    .addTestDevice(AdMobAds.DEVICE_ID_7)
                    .addTestDevice(AdMobAds.DEVICE_ID_8)
                    .addTestDevice(AdMobAds.DEVICE_ID_9)
                    .addTestDevice(AdMobAds.DEVICE_ID_10)
                    .build();

            try {
                adLoader.loadAd(adRequest);
            } catch (Exception e) {
                if (listener != null)
                    listener.onAdFailed(AdsEnum.ADS_ADMOB, e.getMessage());
            }

        } else {
            if (listener != null)
                listener.onAdFailed(AdsEnum.ADS_ADMOB, "NativeAdvancedAds Id null");
        }

    }


    public void showNativeAdvancedAds(final Activity context, String id,
                                      final boolean isNativeLarge, final AppAdsListener listener) {
        if (mainUnifiedNativeAd == null) {
            getNativeAdvancedAds(context, id, isNativeLarge, listener);
        } else {
            final LinearLayout linearLayout = new LinearLayout(context);
            if (isNativeLarge) {
                UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater().inflate(R.layout.ad_admob_native_large, linearLayout, false);
                populateUnifiedNativeAdForLarge(mainUnifiedNativeAd, adView);
                linearLayout.addView(adView);
                listener.onAdLoaded(linearLayout);
            } else {
                UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater().inflate(R.layout.ad_admob_native_medium, linearLayout, false);
                populateUnifiedNativeAdForMedium(mainUnifiedNativeAd, adView);
                linearLayout.addView(adView);
                listener.onAdLoaded(linearLayout);
            }
            getNativeAdvancedAds(context, id, isNativeLarge, null);
        }
    }

    public void showNativeGridAds(final Activity context, String id,
                                  final ViewGroup moreLayout, final AppAdsListener listener) {
        if (mainUnifiedNativeAd == null) {
            getNativeAdvancedAds_GridView_Ads(context, id, moreLayout, listener);

        } else {
            final LinearLayout linearLayout = new LinearLayout(context);
            UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater().inflate(R.layout.ad_admob_grid, linearLayout, false);
            populateUnifiedNativeAdForGrid(mainUnifiedNativeAd, adView);
            linearLayout.addView(adView);
            listener.onAdLoaded(linearLayout);
            if (moreLayout != null) {
                moreLayout.setVisibility(View.GONE);
            }
            getNativeAdvancedAds_GridView_Ads(context, id, moreLayout, null);
        }
    }

    public void showNativeRectangleAds(final Activity context, String id, final AppAdsListener listener) {
        if (mainUnifiedNativeAd == null) {
            getNativeRectangleAds(context, id, listener);

        } else {
            final LinearLayout linearLayout = new LinearLayout(context);
            UnifiedNativeAdView adView = (UnifiedNativeAdView) context.getLayoutInflater().inflate(R.layout.ad_admob_native_rectangle, linearLayout, false);
            populateUnifiedNativeAdForRect(mainUnifiedNativeAd, adView);
            linearLayout.addView(adView);
            listener.onAdLoaded(linearLayout);
            getNativeRectangleAds(context, id, null);
        }
    }
}

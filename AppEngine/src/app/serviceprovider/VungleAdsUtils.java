package app.serviceprovider;

import android.app.Activity;

import app.enginev4.AdsEnum;
import app.listener.AppAdsListener;
import app.listener.AppFullAdsListener;
import app.pnd.adshandler.R;

/**
 * Created by Meenu Singh on 03/06/19.
 */
public class VungleAdsUtils {
    //For test native ads id FLEXFEED-2744806 and app id 5bf49746b1fd5362ddda51e2
    //final private String appId = "5cdd195c6f69b9001109b932";
    // private String testID = "FLEXFEED-2744806";

    private static VungleAdsUtils vungleAdsUtils = null;

    public static VungleAdsUtils getVungleObject() {
        if (vungleAdsUtils == null) {
            synchronized (VungleAdsUtils.class) {
                if (vungleAdsUtils == null) {
                    vungleAdsUtils = new VungleAdsUtils();
                }
            }
        }
        return vungleAdsUtils;
    }

    public void getVungleNativeAds(final Activity ctx, final String id, final AppAdsListener listener) {
        //id = testID;
//        if (id != null && !id.equals("")) {
//            Vungle.init(ctx.getResources().getString(R.string.app_id_vungle), ctx.getApplicationContext(), new InitCallback() {
//                @Override
//                public void onSuccess() {
//                    // Initialization has succeeded and SDK is ready to load an ad or play one if there
//                    // is one pre-cached already
//                    System.out.println("VungleAdsUtils.onSuccess native ");
//                    if (Vungle.isInitialized()) {
//                        Vungle.loadAd(id, new NativeLoadAd(ctx, id, listener));
//                    }
//                }
//
//                @Override
//                public void onError(Throwable throwable) {
//                    // Initialization error occurred - throwable.getLocalizedMessage() contains error message
//                    listener.onAdFailed(AdsEnum.ADS_VUNGLE, throwable.getLocalizedMessage());
//                    //linearLayout.addView(AdMobAds.getAdmobOBJ(ctx).admob_GetBannerRectangleAds(ctx, Slave.ADMOB_BANNER_ID_RECTANGLE_STATIC));
//
//                }
//
//                @Override
//                public void onAutoCacheAdAvailable(String placementId) {
//                    // Callback to notify when an ad becomes available for the auto-cached placement
//                    // NOTE: This callback works only for the auto-cached placement. Otherwise, please use
//                    // LoadAdCallback with loadAd API for loading placements.
//                }
//            });
//        } else {
//            listener.onAdFailed(AdsEnum.ADS_VUNGLE, "Vungle ID blank");
//        }
    }

//    private class NativeLoadAd implements LoadAdCallback {
//        private AppAdsListener listener;
//        private Activity ctx;
//        private String adsID;
//
//        NativeLoadAd(Activity context, String id, AppAdsListener appAdsListener) {
//            this.ctx = context;
//            this.adsID = id;
//            this.listener = appAdsListener;
//        }
//
//        @Override
//        public void onAdLoad(String s) {
//            if (Vungle.isInitialized() && Vungle.canPlayAd(adsID)) {
//                LinearLayout linearLayout = new LinearLayout(ctx);
//                VungleNativeAd vungleNativeAd = Vungle.getNativeAd(adsID, new NativePlayAdCallback(ctx, listener));
//
//                LayoutInflater inflater = LayoutInflater.from(ctx);
//                LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_vungle_native,
//                        linearLayout, false);
//                linearLayout.addView(adView);
//                RelativeLayout flexfeed_container = adView.findViewById(R.id.nativeads);
//
//                if (vungleNativeAd != null)
//                    if (vungleNativeAd.renderNativeView() != null) {
//                        View nativeAdView = vungleNativeAd.renderNativeView();
//                        vungleNativeAd.setAdVisibility(true);
//
//                        if (flexfeed_container != null) {
//                            if (flexfeed_container.getChildCount() > 0)
//                                flexfeed_container.removeAllViews();
//                            flexfeed_container.addView(nativeAdView);
//                            listener.onAdLoaded(flexfeed_container);
//                        }
//
//                    }
//            }
//        }
//
//        @Override
//        public void onError(String s, Throwable throwable) {
//            listener.onAdFailed(AdsEnum.ADS_VUNGLE, throwable.getLocalizedMessage());
//        }
//    }

//    private class NativePlayAdCallback implements PlayAdCallback {
//        AppAdsListener listener;
//        Activity ctx;
//
//        NativePlayAdCallback(Activity context, AppAdsListener appAdsListener) {
//            this.ctx = context;
//            this.listener = appAdsListener;
//        }
//
//        @Override
//        public void onAdStart(String s) {
//
//        }
//
//        @Override
//        public void onAdEnd(String s, boolean b, boolean b1) {
//
//        }
//
//        @Override
//        public void onError(String s, Throwable throwable) {
//            listener.onAdFailed(AdsEnum.ADS_VUNGLE, throwable.getLocalizedMessage());
//        }
//    }


    public void loadVungleFullAds(final Activity ctx, String id, final AppFullAdsListener listener, final boolean isFromCache) {
//        if (id != null && !id.equals("")) {
//            id = id.trim();
//            final String finalId = id;
//            Vungle.init(ctx.getResources().getString(R.string.app_id_vungle), ctx.getApplicationContext(), new InitCallback() {
//                @Override
//                public void onSuccess() {
//                    // Initialization has succeeded and SDK is ready to load an ad or play one if there
//                    // is one pre-cached already
//                    System.out.println("VungleAdsUtils.onSuccess Init");
//                    if (Vungle.isInitialized()) {
//                        Vungle.loadAd(finalId, new LoadAdCallback() {
//                            @Override
//                            public void onAdLoad(String s) {
//                                System.out.println("VungleAdsUtils.onAdLoad " + s);
//                                if (isFromCache)
//                                    listener.onFullAdLoaded();
//                            }
//
//                            @Override
//                            public void onError(String s, Throwable throwable) {
//                                if (isFromCache)
//                                    listener.onFullAdFailed(AdsEnum.FULL_ADS_VUNGLE, throwable.getLocalizedMessage());
//                            }
//                        });
//                    } else {
//                        listener.onFullAdFailed(AdsEnum.FULL_ADS_VUNGLE, String.valueOf(Vungle.isInitialized()));
//                    }
//
//                }
//
//                @Override
//                public void onError(Throwable throwable) {
//                    // Initialization error occurred - throwable.getLocalizedMessage() contains error message
//                    listener.onFullAdFailed(AdsEnum.FULL_ADS_VUNGLE, throwable.getLocalizedMessage());
//                }
//
//                @Override
//                public void onAutoCacheAdAvailable(String placementId) {
//                    System.out.println("VungleAdsUtils.onAutoCacheAdAvailable " + placementId);
//                    // Callback to notify when an ad becomes available for the auto-cached placement
//                    // NOTE: This callback works only for the auto-cached placement. Otherwise, please use
//                    // LoadAdCallback with loadAd API for loading placements.
//                }
//            });
//
//        } else {
//            listener.onFullAdFailed(AdsEnum.FULL_ADS_VUNGLE, "Vungle ID blank");
//        }

        listener.onFullAdFailed(AdsEnum.FULL_ADS_VUNGLE, "not used");
    }

    public void showVungleFullAds(Activity ctx, final String id, final AppFullAdsListener listener) {
//        if (ctx != null && id != null && !id.equals("")) {
//            if (Vungle.isInitialized() && Vungle.canPlayAd(id)) {
//                Vungle.playAd(id, null, new PlayAdCallback() {
//                    @Override
//                    public void onAdStart(String s) {
//                    }
//
//                    @Override
//                    public void onAdEnd(String s, boolean b, boolean b1) {
//                        System.out.println("VungleAdsUtils.onAdEnd");
//                        //loadVungleFullAds(ctx, id, listener);
//                    }
//
//                    @Override
//                    public void onError(String s, Throwable throwable) {
//                        listener.onFullAdFailed(AdsEnum.FULL_ADS_VUNGLE, throwable.getLocalizedMessage());
//
//                    }
//                });
//                listener.onFullAdLoaded();
//            } else {
//                loadVungleFullAds(ctx, id, listener, false);
//                listener.onFullAdFailed(AdsEnum.FULL_ADS_VUNGLE, String.valueOf(Vungle.canPlayAd(id)));
//            }
//        } else {
//            listener.onFullAdFailed(AdsEnum.FULL_ADS_VUNGLE, "Vungle ID blank");
//        }

        listener.onFullAdFailed(AdsEnum.FULL_ADS_VUNGLE, "Not used");
    }

}

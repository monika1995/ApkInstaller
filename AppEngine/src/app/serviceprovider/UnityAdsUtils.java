package app.serviceprovider;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.UnityServices;
import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBanners;
import com.unity3d.services.monetization.IUnityMonetizationListener;
import com.unity3d.services.monetization.UnityMonetization;
import com.unity3d.services.monetization.placementcontent.core.PlacementContent;

import app.enginev4.AdsEnum;
import app.listener.AppAdsListener;
import app.listener.AppFullAdsListener;
import app.pnd.adshandler.R;

/**
 * Created by Meenu Singh on 03/06/19.
 */
public class UnityAdsUtils {

    //private View bannerView;
    /**
     * unity banner ads listener called multiple time that's why
     * we are using onlyOne boolen to call AppAdsListener listener only one time.
     */
    private boolean onlyOne = false;
    private static UnityAdsUtils unityAdsUtils = null;

    public static UnityAdsUtils getUnityObj() {
        if (unityAdsUtils == null) {
            synchronized (UnityAdsUtils.class) {
                if (unityAdsUtils == null) {
                    unityAdsUtils = new UnityAdsUtils();
                }
            }
        }
        return unityAdsUtils;
    }

    public void getUnityAdsBanner(final Activity ctx, String id, final AppAdsListener listener) {
        if (id != null && !id.equals("")) {
            id = id.trim();
            onlyOne = false;
            UnityBanners.destroy();
            UnityMonetization.initialize(ctx, ctx.getResources().getString(R.string.app_id_unity), new IUnityMonetizationListener() {
                @Override
                public void onPlacementContentReady(String s, PlacementContent placementContent) {
                    System.out.println("UnityAdsUtils.onPlacementContentReady " + s);
                }

                @Override
                public void onPlacementContentStateChange(String s, PlacementContent placementContent,
                                                          UnityMonetization.PlacementContentState placementContentState,
                                                          UnityMonetization.PlacementContentState placementContentState1) {

                    System.out.println("UnityAdsUtils.onPlacementContentStateChange " + s);
                }

                @Override
                public void onUnityServicesError(UnityServices.UnityServicesError unityServicesError, String s) {
                    System.out.println("UnityAdsUtils.onUnityServicesError " + unityServicesError.toString() + " " + s);
                }
            });

            UnityBanners.setBannerListener(new UnityBannerListener(ctx, listener));
            UnityBanners.loadBanner(ctx, id);
        } else {
            listener.onAdFailed(AdsEnum.ADS_UNITY, "Unity ID blank");
        }
    }

    private class UnityBannerListener implements IUnityBannerListener {
        private AppAdsListener listener;
        private Activity ctx;
        private LinearLayout linearLayout;

        UnityBannerListener(Activity context, AppAdsListener appAdsListener) {
            this.ctx = context;
            this.listener = appAdsListener;
            linearLayout = new LinearLayout(ctx);
        }

        @Override
        public void onUnityBannerLoaded(String s, View view) {
            try {
                System.out.println("UnityBannerListener.onUnityBannerLoaded " + s);
                linearLayout.removeAllViews();
                linearLayout.addView(view);
                listener.onAdLoaded(linearLayout);
            } catch (Exception e) {
                System.out.println("UnityBannerListener.onUnityBannerLoaded " + e.getMessage());
                listener.onAdFailed(AdsEnum.ADS_UNITY, "Cannot add a null child view to a ViewGroup");
            }
        }

        @Override
        public void onUnityBannerUnloaded(String s) {
            System.out.println("UnityBannerListener.onUnityBannerUnloaded ");
        }

        @Override
        public void onUnityBannerShow(String s) {

        }

        @Override
        public void onUnityBannerClick(String s) {

        }

        @Override
        public void onUnityBannerHide(String s) {
            System.out.println("UnityBannerListener.onUnityBannerHide " + ctx.getClass());
        }

        @Override
        public void onUnityBannerError(String s) {
            if (!onlyOne) {
                listener.onAdFailed(AdsEnum.ADS_UNITY, s);
                onlyOne = true;
            }

        }


    }

    public void loadUnityFullAds(final Activity ctx, final String placementId, final AppFullAdsListener listener, final boolean isFromCache) {
        if (placementId != null && !placementId.equals("")) {
            System.out.println("UnityAdsUtils.loadUnityFullAds " + placementId + " " + UnityAds.isInitialized());
            if (!UnityAds.isInitialized()) {
                UnityAds.initialize(ctx, ctx.getResources().getString(R.string.app_id_unity), new IUnityAdsListener() {
                    @Override
                    public void onUnityAdsReady(String s) {
                        System.out.println("UnityAdsUtils.onUnityAdsReady " + s);
                        if (UnityAds.isReady(placementId)) {
                            if (isFromCache)
                                listener.onFullAdLoaded();
                        }
                    }

                    @Override
                    public void onUnityAdsStart(String s) {
                    }

                    @Override
                    public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
                        //loadUnityFullAds(ctx, placementId, listener);
                    }

                    @Override
                    public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
                        if (isFromCache)
                            listener.onFullAdFailed(AdsEnum.FULL_ADS_UNITY, unityAdsError.name());
                    }
                });
            }

        } else {
            listener.onFullAdFailed(AdsEnum.FULL_ADS_UNITY, "Unity ID blank");
        }
    }

    public void showUnityFullAds(Activity ctx, String placementId, AppFullAdsListener listener) {
        if (placementId != null && !placementId.equals("")) {
            if (UnityAds.isReady(placementId)) {
                UnityAds.show(ctx);
                listener.onFullAdLoaded();
            } else {
                loadUnityFullAds(ctx, placementId, listener, false);
                listener.onFullAdFailed(AdsEnum.FULL_ADS_UNITY, String.valueOf(UnityAds.isReady(placementId)));
            }
        } else {
            listener.onFullAdFailed(AdsEnum.FULL_ADS_UNITY, "Unity ID blank");
        }
    }

    public void UnityAdsDestroy() {
        try {
            UnityBanners.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

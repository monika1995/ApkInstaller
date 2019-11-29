package app.serviceprovider;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.ads.banner.BannerListener;
import com.startapp.android.publish.ads.nativead.NativeAdDetails;
import com.startapp.android.publish.ads.nativead.NativeAdPreferences;
import com.startapp.android.publish.ads.nativead.StartAppNativeAd;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.VideoListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import java.util.ArrayList;

import app.PrintLog;
import app.enginev4.AdsEnum;
import app.listener.AppAdsListener;
import app.listener.AppFullAdsListener;
import app.pnd.adshandler.R;

import static app.serviceprovider.Utils.get_FulladsshownCount;
import static app.serviceprovider.Utils.set_FulladsshownCount;


public class StartupAdsProvider {

    private StartAppAd startAppAd;
    private static NativeAdDetails nativeAd = null;
    private static StartupAdsProvider startupAdsProvider = null;

    public static StartupAdsProvider getStartappObj(Activity activity, String appid) {
        if (startupAdsProvider == null) {
            synchronized (StartupAdsProvider.class) {
                if (startupAdsProvider == null) {
                    startupAdsProvider = new StartupAdsProvider(activity, appid);
                }
            }
        }
        return startupAdsProvider;
    }

    private StartupAdsProvider(Activity activity, String appid) {
        appid = appid.trim();
        startAppAd = new StartAppAd(activity);
        StartAppSDK.init(activity, appid, false);

        StartAppAd.disableSplash();
        PrintLog.print("Init for " + appid);
    }

    public void getBannerAds(final Activity context, final AppAdsListener listener) {
        PrintLog.print("call banner");
        final Banner startAppBanner = new Banner(context);
        final RelativeLayout.LayoutParams bannerParameters = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        bannerParameters.addRule(RelativeLayout.CENTER_IN_PARENT | RelativeLayout.CENTER_HORIZONTAL);

        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.addView(startAppBanner, bannerParameters);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        startAppBanner.setBannerListener(new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                PrintLog.print("StartupAdsProvider.onReceiveAd ");
                startAppBanner.showBanner();

            }

            @Override
            public void onFailedToReceiveAd(View view) {
                listener.onAdFailed(AdsEnum.ADS_STARTUP, "Startup Banner Failed");
                PrintLog.print("StartupAdsProvider.onFailedToReceiveAd ");
            }

            @Override
            public void onClick(View view) {
                PrintLog.print("StartupAdsProvider.onClick ");

            }
        });
        /*
         * issue when we tring to load on onReceiveAd listener.
         */
        listener.onAdLoaded(linearLayout);
    }

    private void showVideoAds(final AppFullAdsListener listener) {
        if (startAppAd.isReady()) {
            System.out.println("StartupAdsProvider.showVideoAds");
            startAppAd.showAd();
            listener.onFullAdLoaded();
        } else {
            System.out.println("StartupAdsProvider.showVideoAds not ");
            listener.onFullAdFailed(AdsEnum.FULL_ADS_STARTUP, "startAppAd isReady false");
            loadFullAds(listener);
        }
      /*  startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            public void onReceiveAd(Ad arg0) {
                System.out.println("StartupAdsProvider.onReceiveAd ");
                listener.onFullAdLoaded();
                startAppAd.showAd();
            }

            public void onFailedToReceiveAd(Ad arg0) {
                System.out.println("StartupAdsProvider.onFailedToReceiveAd ");
                listener.onFullAdFailed(AdsEnum.FULL_ADS_STARTUP, "Video Ads Failed");
            }
        });*/

    }

    public void loadFullAds(final AppFullAdsListener listener) {
        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
            public void onReceiveAd(Ad arg0) {
                System.out.println("StartupAdsProvider.onReceiveAd ");
                listener.onFullAdLoaded();
            }

            public void onFailedToReceiveAd(Ad arg0) {
                System.out.println("StartupAdsProvider.onFailedToReceiveAd ");
                listener.onFullAdFailed(AdsEnum.FULL_ADS_STARTUP, "Video Ads Failed");
            }
        });

        startAppAd.setVideoListener(new VideoListener() {
            public void onVideoCompleted() {

            }
        });
    }

    public void showfullAds(final Activity activity, int Videoadscount, final AppFullAdsListener listener) {
        int currentcount = get_FulladsshownCount(activity);
        set_FulladsshownCount(activity, currentcount + 1);
        PrintLog.print("currentcount " + currentcount + "Videoadscount "
                + Videoadscount);
        PrintLog.print("1736 start app 01");
        try {
            if (currentcount % Videoadscount == 0) {
                PrintLog.print("1736 start app 02");
                showVideoAds(listener);

            } else {
                PrintLog.print("1736 start app 03ma");
                PrintLog.print("IS Ready " + startAppAd.isReady());

                if (startAppAd != null) {
                    startAppAd.loadAd(new AdEventListener() {
                        @Override
                        public void onReceiveAd(Ad ad) {
                            PrintLog.print("Startapp full ads on ads success");
                            startAppAd.showAd();
                            listener.onFullAdLoaded();
                        }

                        @Override
                        public void onFailedToReceiveAd(Ad ad) {
                            PrintLog.print("Startapp full ads on ads failure");
                            listener.onFullAdFailed(AdsEnum.FULL_ADS_STARTUP, ad.getErrorMessage());

                        }
                    });


                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void showNativeMedium(final Context context, String adsID, final AppAdsListener listener) {
        System.out.println("StartupAdsProvider.showNativeMedium " + adsID);
        final LinearLayout nativeAdContainer = new LinearLayout(context);
        nativeAdContainer.setOrientation(LinearLayout.VERTICAL);
        // creating LayoutParams
        AbsListView.LayoutParams linLayoutParam = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        nativeAdContainer.setLayoutParams(linLayoutParam);

        final StartAppNativeAd startAppNativeAd = new StartAppNativeAd(context);
        startAppNativeAd.loadAd(
                new NativeAdPreferences()
                        .setAdsNumber(1)
                        .setAutoBitmapDownload(true)
                        .setPrimaryImageSize(2),
                new AdEventListener() {
                    @Override
                    public void onReceiveAd(Ad ad) {
                        PrintLog.print("0956 checking 01");
                        LayoutInflater inflater = LayoutInflater.from(context);
                        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_startup_native_medium,
                                nativeAdContainer, false);
                        nativeAdContainer.removeAllViews();
                        nativeAdContainer.addView(adView);


                        ArrayList<NativeAdDetails> nativeAdsList = startAppNativeAd.getNativeAds();
                        if (nativeAdsList.size() > 0) {
                            nativeAd = nativeAdsList.get(0);
                        }

                        // Verify that an ad was retrieved
                        if (nativeAd != null) {

                            // When ad is received and displayed - we MUST send impression
                            nativeAd.sendImpression(context);


                            LinearLayout adsParent = adView.findViewById(R.id.native_ad_unit);
                            adsParent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nativeAd.sendClick(context);
                                }
                            });

                            ImageView contentad_image = adView.findViewById(R.id.native_ad_icon);
                            contentad_image.setImageBitmap(nativeAd.getImageBitmap());

                            TextView contentad_headline = adView.findViewById(R.id.native_ad_title);
                            contentad_headline.setText(nativeAd.getTitle());

                            TextView contentad_body = adView.findViewById(R.id.native_ad_body);
                            contentad_body.setText(nativeAd.getDescription());

                            listener.onAdLoaded(nativeAdContainer);
                        }
                    }

                    @Override
                    public void onFailedToReceiveAd(Ad ad) {
                        listener.onAdFailed(AdsEnum.ADS_STARTUP, ad.getErrorMessage());

                    }
                });

    }

    public void showNativeLarge(final Context context, final String appID, final AppAdsListener listener) {
        System.out.println("StartupAdsProvider.showNativeLarge " + appID);
        final LinearLayout nativeAdContainerlarge = new LinearLayout(context);
        nativeAdContainerlarge.setOrientation(LinearLayout.VERTICAL);
        // creating LayoutParams
        AbsListView.LayoutParams linLayoutParam = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        nativeAdContainerlarge.setLayoutParams(linLayoutParam);

        final StartAppNativeAd startAppNativeAd = new StartAppNativeAd(context);
        final NativeAdDetails[] nativeAd = {null};

        startAppNativeAd.loadAd(
                new NativeAdPreferences()
                        .setAdsNumber(1)
                        .setAutoBitmapDownload(true)
                        .setPrimaryImageSize(3),
                new AdEventListener() {
                    @Override
                    public void onReceiveAd(Ad ad) {
                        PrintLog.print("0956 checking 01");
                        LayoutInflater inflater = LayoutInflater.from(context);
                        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_startup_native_large,
                                nativeAdContainerlarge, false);

                        nativeAdContainerlarge.removeAllViews();
                        nativeAdContainerlarge.addView(adView);


                        ArrayList<NativeAdDetails> nativeAdsList = startAppNativeAd.getNativeAds();
                        if (nativeAdsList.size() > 0) {
                            nativeAd[0] = nativeAdsList.get(0);
                        }

                        // Verify that an ad was retrieved
                        if (nativeAd[0] != null) {

                            // When ad is received and displayed - we MUST send impression
                            nativeAd[0].sendImpression(context);


                            LinearLayout adsParent = adView.findViewById(R.id.native_ad_unit);
                            adsParent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nativeAd[0].sendClick(context);
                                }
                            });

                            ImageView contentad_image = adView.findViewById(R.id.native_ad_icon);
                            contentad_image.setImageBitmap(nativeAd[0].getImageBitmap());

                            TextView contentad_headline = adView.findViewById(R.id.native_ad_title);
                            contentad_headline.setText(nativeAd[0].getTitle());

                            TextView contentad_body = adView.findViewById(R.id.native_ad_body);
                            contentad_body.setText(nativeAd[0].getDescription());

                            listener.onAdLoaded(nativeAdContainerlarge);
                        }
                    }

                    @Override
                    public void onFailedToReceiveAd(Ad ad) {
                        listener.onAdFailed(AdsEnum.ADS_STARTUP, ad.getErrorMessage());

                    }
                });


    }


    public void showNativeMedium_Grid(final Context context, String adsID, final ViewGroup moreLayout, final AppAdsListener listener) {
        adsID = adsID.trim();
        System.out.println("StartupAdsProvider.showNativeMedium_Grid " + adsID);
        final LinearLayout nativeAdContainer = new LinearLayout(context);

        final StartAppNativeAd startAppNativeAd = new StartAppNativeAd(context);
        startAppNativeAd.loadAd(
                new NativeAdPreferences()
                        .setAdsNumber(1)
                        .setAutoBitmapDownload(true)
                        .setPrimaryImageSize(2),
                new AdEventListener() {
                    @Override
                    public void onReceiveAd(Ad ad) {
                        System.out.println("0956 checking 01");
                        LayoutInflater inflater = LayoutInflater.from(context);
                        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_startup_grid,
                                nativeAdContainer, false);
                        nativeAdContainer.addView(adView);


                        ArrayList<NativeAdDetails> nativeAdsList = startAppNativeAd.getNativeAds();
                        if (nativeAdsList.size() > 0) {
                            nativeAd = nativeAdsList.get(0);
                        }

                        // Verify that an ad was retrieved
                        if (nativeAd != null) {

                            // When ad is received and displayed - we MUST send impression
                            nativeAd.sendImpression(context);


                            LinearLayout adsParent = adView.findViewById(R.id.native_ad_unit);
                            adsParent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nativeAd.sendClick(context);
                                }
                            });

                            Button clickbutton = adView.findViewById(R.id.native_install);
                            clickbutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    nativeAd.sendClick(context);
                                }
                            });

                            ImageView contentad_image = adView.findViewById(R.id.native_ad_icon);
                            contentad_image.setImageBitmap(nativeAd.getImageBitmap());

                            TextView contentad_headline = adView.findViewById(R.id.native_ad_title);
                            contentad_headline.setText(nativeAd.getTitle());

                            TextView contentad_body = adView.findViewById(R.id.native_ad_body);
                            contentad_body.setText(nativeAd.getDescription());

                            System.out.println("0956 checking 03");
                            if (moreLayout != null) {
                                moreLayout.setVisibility(View.GONE);
                            }

                        }
                        listener.onAdLoaded(nativeAdContainer);

                    }

                    @Override
                    public void onFailedToReceiveAd(Ad ad) {
                        listener.onAdFailed(AdsEnum.ADS_STARTUP, ad.getErrorMessage());

                    }
                });

    }

}

package app.serviceprovider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.adshandler.FullPagePromo;
import app.campaign.CampaignConstant;
import app.campaign.CampaignHandler;
import app.campaign.response.AdsIcon;
import app.enginev4.AdsEnum;
import app.listener.AppAdsListener;
import app.listener.AppFullAdsListener;
import app.pnd.adshandler.R;
import app.rest.request.DataRequest;
import app.server.v2.DataHubHandler;
import app.server.v2.InHouse;
import app.server.v2.Slave;
import app.socket.EngineApiController;
import app.socket.EngineClient;
import app.socket.Response;

/**
 * Created by rajeev on 29/06/17.
 */

public class InHouseAds {
    public static final String TYPE_NATIVE_MEDIUM = EngineClient.IH_NM;
    public static final String TYPE_BANNER_HEADER = EngineClient.IH_TOP_BANNER;
    public static final String TYPE_BANNER_FOOTER = EngineClient.IH_BOTTOM_BANNER;
    public static final String TYPE_NATIVE_LARGE = EngineClient.IH_NL;
    public static final String TYPE_BANNER_LARGE = EngineClient.IH_BANNER_LARGE;
    public static final String TYPE_BANNER_RECTANGLE = EngineClient.IH_BANNER_RECTANGLE;

    //public static String TYPE_CP_START = EngineClient.IH_CP_START;
    //public static String TYPE_CP_EXIT = EngineClient.IH_CP_EXIT;

    private Display display;
    private String clickBF, clickBH, clickBL, clickBR, clickNM, clickNL;

    public void getBannerFooter(final Context context, String type, final AppAdsListener listener) {
        display = ((Activity) context).getWindowManager().getDefaultDisplay();

        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.banner_height));
        final LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(params);


        DataRequest request = new DataRequest();
        EngineApiController controller = new EngineApiController(context, new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                System.out.println("here is the response of INHOUSE" + " " + response);
                new DataHubHandler().parseInHouseService(context, response.toString(), new DataHubHandler.InHouseCallBack() {
                    @Override
                    public void onInhouseDownload(InHouse inHouse) {
                        if (inHouse.campType != null && !inHouse.campType.equals("")) {
                            if (inHouse.campType.equalsIgnoreCase("html")) {
                                LayoutInflater inflater = LayoutInflater.from(context);
                                LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_inhouse_web,
                                        ll, false);

                                populateWebView(adView, inHouse.html);
                                ll.addView(adView);
                                listener.onAdLoaded(ll);

                            } else {
                                ImageView iv = new ImageView(context);
                                iv.setLayoutParams(params);
                                ll.addView(iv);
                                if (inHouse.src != null && !inHouse.src.isEmpty()) {
                                    Picasso.get()
                                            .load(inHouse.src)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                            .resize(display.getWidth(), iv.getHeight())
                                            .placeholder(R.drawable.blank)
                                            .into(iv);
                                    Drawable bmp = iv.getDrawable();
                                    ll.setOrientation(LinearLayout.HORIZONTAL);
                                    ll.setBackground(bmp);
                                    listener.onAdLoaded(ll);
                                } else {
                                    listener.onAdFailed(AdsEnum.ADS_INHOUSE, " INHOUSE SRC NULL ");
                                }

                                if (inHouse.clicklink != null && !inHouse.clicklink.isEmpty()) {
                                    clickBF = inHouse.clicklink;
                                }
                            }
                        }


                    }
                });
            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                listener.onAdFailed(AdsEnum.ADS_INHOUSE, errormsg);
            }
        }, EngineApiController.INHOUSE_CODE);
        controller.setInHouseType(type);
        controller.getInHouseData(request);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickBF != null && !clickBF.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(clickBF));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
                }
            }
        });

    }

    public void getBannerHeader(final Context context, String type, final AppAdsListener listener) {
        display = ((Activity) context).getWindowManager().getDefaultDisplay();

        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.banner_height));
        final LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(params);


        DataRequest request = new DataRequest();
        EngineApiController controller = new EngineApiController(context, new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                System.out.println("here is the response of INHOUSE banner header" + " " + response);
                new DataHubHandler().parseInHouseService(context, response.toString(), new DataHubHandler.InHouseCallBack() {
                    @Override
                    public void onInhouseDownload(InHouse inHouse) {
                        if (inHouse.campType != null && !inHouse.campType.equals("")) {
                            if (inHouse.campType.equalsIgnoreCase("html")) {
                                LayoutInflater inflater = LayoutInflater.from(context);
                                LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_inhouse_web,
                                        ll, false);

                                populateWebView(adView, inHouse.html);
                                ll.addView(adView);
                                listener.onAdLoaded(ll);

                            } else {
                                ImageView iv = new ImageView(context);
                                iv.setLayoutParams(params);
                                ll.addView(iv);
                                if (inHouse.src != null && !inHouse.src.isEmpty()) {
                                    Picasso.get()
                                            .load(inHouse.src)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                            .resize(display.getWidth(), iv.getHeight())
                                            .placeholder(R.drawable.blank)
                                            .into(iv);
                                    Drawable bmp = iv.getDrawable();
                                    ll.setOrientation(LinearLayout.HORIZONTAL);
                                    ll.setBackground(bmp);
                                    listener.onAdLoaded(ll);
                                } else {
                                    listener.onAdFailed(AdsEnum.ADS_INHOUSE, " Inhouse src null ");
                                }

                                if (inHouse.clicklink != null && !inHouse.clicklink.isEmpty()) {
                                    clickBH = inHouse.clicklink;
                                }
                            }
                        } else {
                            listener.onAdFailed(AdsEnum.ADS_INHOUSE, " Inhouse campType null or not valid ");
                        }


                    }
                });
            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                listener.onAdFailed(AdsEnum.ADS_INHOUSE, errormsg);
            }
        }, EngineApiController.INHOUSE_CODE);
        controller.setInHouseType(type);
        controller.getInHouseData(request);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickBH != null && !clickBH.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(clickBH));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
                }
            }
        });

    }

    public void getBannerLarge(final Context context, String type, final AppAdsListener listener) {
        display = ((Activity) context).getWindowManager().getDefaultDisplay();

        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.banner_large_height));
        final LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(params);


        DataRequest request = new DataRequest();
        EngineApiController controller = new EngineApiController(context, new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                System.out.println("here is the response of INHOUSE" + " " + response);
                new DataHubHandler().parseInHouseService(context, response.toString(), new DataHubHandler.InHouseCallBack() {
                    @Override
                    public void onInhouseDownload(InHouse inHouse) {
                        if (inHouse.campType != null && !inHouse.campType.equals("")) {
                            if (inHouse.campType.equalsIgnoreCase("html")) {
                                LayoutInflater inflater = LayoutInflater.from(context);
                                LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_inhouse_web,
                                        ll, false);

                                populateWebView(adView, inHouse.html);
                                ll.addView(adView);
                                listener.onAdLoaded(ll);


                            } else {
                                ImageView iv = new ImageView(context);
                                iv.setLayoutParams(params);
                                ll.addView(iv);
                                if (inHouse.src != null && !inHouse.src.isEmpty()) {
                                    Picasso.get()
                                            .load(inHouse.src)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                            .resize(display.getWidth(), iv.getHeight())
                                            .placeholder(R.drawable.blank)
                                            .into(iv);
                                    Drawable bmp = iv.getDrawable();
                                    ll.setOrientation(LinearLayout.HORIZONTAL);
                                    ll.setBackground(bmp);
                                    listener.onAdLoaded(ll);
                                } else {
                                    listener.onAdFailed(AdsEnum.ADS_INHOUSE, " INHOUSE SRC NULL ");
                                }

                                if (inHouse.clicklink != null && !inHouse.clicklink.isEmpty()) {
                                    clickBL = inHouse.clicklink;
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                listener.onAdFailed(AdsEnum.ADS_INHOUSE, errormsg);
            }
        }, EngineApiController.INHOUSE_CODE);
        controller.setInHouseType(type);
        controller.getInHouseData(request);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickBL != null && !clickBL.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(clickBL));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
                }
            }
        });

    }

    public void getBannerRectangle(final Context context, String type, final AppAdsListener listener) {
        display = ((Activity) context).getWindowManager().getDefaultDisplay();

        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) context.getResources().getDimension(R.dimen.banner_rectangle_width), (int) context.getResources().getDimension(R.dimen.banner_rectangle_height));
        final LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(params);


        DataRequest request = new DataRequest();
        EngineApiController controller = new EngineApiController(context, new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                System.out.println("here is the response of INHOUSE" + " " + response);
                new DataHubHandler().parseInHouseService(context, response.toString(), new DataHubHandler.InHouseCallBack() {
                    @Override
                    public void onInhouseDownload(InHouse inHouse) {
                        if (inHouse.campType != null && !inHouse.campType.equals("")) {
                            if (inHouse.campType.equalsIgnoreCase("html")) {
                                LayoutInflater inflater = LayoutInflater.from(context);
                                LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_inhouse_web,
                                        ll, false);

                                populateWebView(adView, inHouse.html);
                                ll.addView(adView);
                                listener.onAdLoaded(ll);


                            } else {
                                ImageView iv = new ImageView(context);
                                iv.setLayoutParams(params);
                                ll.addView(iv);
                                if (inHouse.src != null && !inHouse.src.isEmpty()) {
                                    Picasso.get()
                                            .load(inHouse.src)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                            .resize(display.getWidth(), iv.getHeight())
                                            .placeholder(R.drawable.blank)
                                            .into(iv);
                                    Drawable bmp = iv.getDrawable();
                                    ll.setOrientation(LinearLayout.HORIZONTAL);
                                    ll.setBackground(bmp);
                                    listener.onAdLoaded(ll);
                                } else {
                                    listener.onAdFailed(AdsEnum.ADS_INHOUSE, " INHOUSE SRC NULL ");
                                }

                                if (inHouse.clicklink != null && !inHouse.clicklink.isEmpty()) {
                                    clickBR = inHouse.clicklink;
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                listener.onAdFailed(AdsEnum.ADS_INHOUSE, errormsg);
            }
        }, EngineApiController.INHOUSE_CODE);
        controller.setInHouseType(type);
        controller.getInHouseData(request);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickBR != null && !clickBR.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(clickBR));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
                }
            }
        });

    }

    public void showNativeMedium(final Context context, String type, final AppAdsListener listener) {
        display = ((Activity) context).getWindowManager().getDefaultDisplay();

        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(params);

        DataRequest request = new DataRequest();
        EngineApiController controller = new EngineApiController(context, new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                System.out.println("here is the response of INHOUSE" + " " + response);
                new DataHubHandler().parseInHouseService(context, response.toString(), new DataHubHandler.InHouseCallBack() {
                    @Override
                    public void onInhouseDownload(InHouse inHouse) {
                        if (inHouse.campType != null && !inHouse.campType.equals("")) {
                            if (inHouse.campType.equalsIgnoreCase("html")) {
                                LayoutInflater inflater = LayoutInflater.from(context);
                                LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_inhouse_web,
                                        ll, false);

                                populateWebView(adView, inHouse.html);
                                ll.addView(adView);
                                listener.onAdLoaded(ll);


                            } else {
                                ImageView iv = new ImageView(context);
                                iv.setLayoutParams(params);
                                ll.addView(iv);
                                if (inHouse.src != null && !inHouse.src.isEmpty()) {
                                    Picasso.get()
                                            .load(inHouse.src)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                            .resize(display.getWidth(), iv.getHeight())
                                            .placeholder(R.drawable.blank)
                                            .into(iv);
                                    Drawable bmp = iv.getDrawable();
                                    ll.setOrientation(LinearLayout.HORIZONTAL);
                                    ll.setBackground(bmp);
                                    listener.onAdLoaded(ll);
                                } else {
                                    listener.onAdFailed(AdsEnum.ADS_INHOUSE, " INHOUSE SRC NULL ");
                                }

                                if (inHouse.clicklink != null && !inHouse.clicklink.isEmpty()) {
                                    clickNM = inHouse.clicklink;
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                listener.onAdFailed(AdsEnum.ADS_INHOUSE, errormsg);

            }
        }, EngineApiController.INHOUSE_CODE);
        controller.setInHouseType(type);
        controller.getInHouseData(request);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickNM != null && !clickNM.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(clickNM));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
                }
            }
        });

    }

    public void showNativeLarge(final Context context, String type, final AppAdsListener listener) {
        display = ((Activity) context).getWindowManager().getDefaultDisplay();

        final ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout ll = new LinearLayout(context);
        ll.setLayoutParams(params);


        DataRequest request = new DataRequest();
        EngineApiController controller = new EngineApiController(context, new Response() {
            @Override
            public void onResponseObtained(Object response, int responseType, boolean isCachedData) {
                System.out.println("here is the response of INHOUSE" + " " + response);
                new DataHubHandler().parseInHouseService(context, response.toString(), new DataHubHandler.InHouseCallBack() {
                    public void onInhouseDownload(InHouse inHouse) {
                        if (inHouse.campType != null && !inHouse.campType.equals("")) {
                            if (inHouse.campType.equalsIgnoreCase("html")) {
                                LayoutInflater inflater = LayoutInflater.from(context);
                                LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_inhouse_web,
                                        ll, false);

                                populateWebView(adView, inHouse.html);
                                ll.addView(adView);
                                listener.onAdLoaded(ll);


                            } else {
                                ImageView iv = new ImageView(context);
                                iv.setLayoutParams(params);
                                ll.addView(iv);
                                if (inHouse.src != null && !inHouse.src.isEmpty()) {
                                    Picasso.get()
                                            .load(inHouse.src)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                            .resize(display.getWidth(), iv.getHeight())
                                            .placeholder(R.drawable.blank)
                                            .into(iv);
                                    Drawable bmp = iv.getDrawable();
                                    ll.setOrientation(LinearLayout.HORIZONTAL);
                                    ll.setBackground(bmp);
                                    listener.onAdLoaded(ll);
                                } else {
                                    listener.onAdFailed(AdsEnum.ADS_INHOUSE, " INHOUSE SRC NULL ");
                                }

                                if (inHouse.clicklink != null && !inHouse.clicklink.isEmpty()) {
                                    clickNL = inHouse.clicklink;
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onErrorObtained(String errormsg, int responseType) {
                listener.onAdFailed(AdsEnum.ADS_INHOUSE, errormsg);

            }
        }, EngineApiController.INHOUSE_CODE);
        controller.setInHouseType(type);
        controller.getInHouseData(request);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickNL != null && !clickNL.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(clickNL));
                    browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(browserIntent);
                }
            }
        });

    }

    public void loadGridViewNativeAdsView(final Activity ctx, String id, final ViewGroup moreLayout, AppAdsListener listener) {
        System.out.println("InHouseAds.loadGridViewNativeAdsView " + id);
        LinearLayout nativeAdContainer = new LinearLayout(ctx);
        LayoutInflater inflater = LayoutInflater.from(ctx);
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_inhouse_grid_layout,
                nativeAdContainer, false);

        ImageView icon = adView.findViewById(R.id.inhouse_img);
        TextView title = adView.findViewById(R.id.inhouse_text);
        RelativeLayout rl = adView.findViewById(R.id.inhouse_lay);

        CampaignHandler handler = CampaignHandler.getInstance();
        final CampaignConstant campaignConstant = new CampaignConstant(ctx);
        final ArrayList<AdsIcon> list = handler.loadIconAdsList();
        if (list != null && list.size() > 0) {
            moreLayout.setVisibility(View.GONE);
            if (list.get(0).src != null && !list.get(0).src.equalsIgnoreCase("") &&
                    list.get(0).src.startsWith("http:")) {
                Picasso.get().load(list.get(0).src).placeholder(R.drawable.app_icon).into(icon);
            }

            if (!list.get(0).srctext.equalsIgnoreCase("")) {
                title.setText(list.get(0).srctext);
            }

            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Slave.hasPurchased(ctx)) {
                        campaignConstant.crossPromotionDialog(ctx,
                                new CampaignConstant.OnCPDialogClick() {
                                    @Override
                                    public void clickOK() {
                                        if (list.size() > 0) {
                                            if (list.get(0).subtype.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_DEEPLINK)) {
                                                campaignConstant.openDeepLink(ctx, list.get(0).page_id);
                                            } else if (list.get(0).subtype.equalsIgnoreCase(CampaignConstant.CAMPAIGN_TYPE_URL)) {
                                                if (list.get(0).clickurl != null && !list.get(0).clickurl.equalsIgnoreCase("") &&
                                                        list.get(0).clickurl.startsWith("http:")) {
                                                    campaignConstant.openURL(ctx, list.get(0).clickurl);
                                                }

                                            }
                                        } else {
                                            new Utils().moreApps(ctx);
                                        }
                                    }

                                }, list.get(0).headertext, list.get(0).description, list.get(0).bgcolor, list.get(0).textcolor, list.get(0).src);
                    }
                }
            });

            listener.onAdLoaded(nativeAdContainer);

        } else {
            listener.onAdFailed(AdsEnum.ADS_INHOUSE, "list can't be null or list size will be > 0 ");
        }


    }

    public void showFullAds(Context context, String type, String src, String link, AppFullAdsListener listener) {
        System.out.println("InHouseAds.showFullAds " + type + " " + src + " " + link);
        if (type != null /*&& src != null && link != null*/) {
            Intent intent = new Intent(context, FullPagePromo.class);
            intent.putExtra("type", type);
            intent.putExtra("src", src);
            intent.putExtra("link", link);
            context.startActivity(intent);
            listener.onFullAdLoaded();
        } else {
            listener.onFullAdFailed(AdsEnum.FULL_ADS_INHOUSE, "type null ");
        }

    }

    private void populateWebView(LinearLayout adView, String data) {
        WebView webView = adView.findViewById(R.id.webView);
        //webView.loadUrl(url);
        webView.loadData(data, "text/html", null);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        // webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new NavWebViewClient());

    }

}

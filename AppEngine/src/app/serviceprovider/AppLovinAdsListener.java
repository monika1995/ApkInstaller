package app.serviceprovider;

import com.applovin.adview.AppLovinAdView;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdLoadListener;

import app.enginev4.AdsEnum;
import app.listener.AppAdsListener;

/**
 * Created by Meenu Singh on 19/06/19.
 */
public class AppLovinAdsListener implements AppLovinAdLoadListener {

    private final AppLovinAdView mAdView;
    private final AppAdsListener mAppAdListener;

    AppLovinAdsListener(AppLovinAdView mAdView, AppAdsListener mOnAppAdListener) throws Exception {
        this.mAdView = mAdView;
        this.mAppAdListener = mOnAppAdListener;

        if (mAdView == null || mOnAppAdListener == null) {
            throw new Exception("AdView and AppAdsListener cannot be null ");
        }
    }

    @Override
    public void adReceived(AppLovinAd ad) {
        //LinearLayout linearLayout = getAdViewLayout();
        mAppAdListener.onAdLoaded(mAdView);

    }

    @Override
    public void failedToReceiveAd(int errorCode) {
        mAppAdListener.onAdFailed(AdsEnum.ADS_APPLOVIN, String.valueOf(errorCode));
    }


   /* private LinearLayout getAdViewLayout(AppLovinAdView mAdView) {
        final LinearLayout linLayout = new LinearLayout(mAdView.getContext());
        linLayout.setOrientation(LinearLayout.VERTICAL);
        // creating LayoutParams
        ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linLayout.setGravity(Gravity.CENTER);
        linLayout.setLayoutParams(linLayoutParam);
        mAdView.setGravity(RelativeLayout.CENTER_IN_PARENT);
        linLayout.addView(mAdView);
        return linLayout;
    }*/


}

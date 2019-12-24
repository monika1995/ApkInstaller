package app.serviceprovider;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdView;

import app.enginev4.AdsEnum;
import app.listener.AppAdsListener;

/**
 * Created by Meenu Singh on 10/06/19.
 */
public class FbAdsListener implements AdListener {

    private final AdView mAdView;
    private final AppAdsListener mAppAdListener;

    FbAdsListener(AdView adView, AppAdsListener adsListener) throws Exception {
        this.mAdView = adView;
        this.mAppAdListener = adsListener;

        if (mAdView == null || mAppAdListener == null) {
            throw new Exception("AdView and AppAdsListener cannot be null ");
        }
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        mAppAdListener.onAdFailed(AdsEnum.ADS_FACEBOOK,adError.getErrorMessage());
    }

    @Override
    public void onAdLoaded(Ad ad) {
        LinearLayout adViewLayout = getAdViewLayout();
        mAppAdListener.onAdLoaded(adViewLayout);
    }

    @Override
    public void onAdClicked(Ad ad) {

    }

    @Override
    public void onLoggingImpression(Ad ad) {

    }

    private LinearLayout getAdViewLayout() {
        final LinearLayout linLayout = new LinearLayout(mAdView.getContext());
        linLayout.setOrientation(LinearLayout.VERTICAL);
        // creating LayoutParams
        ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linLayout.setGravity(Gravity.CENTER);
        linLayout.setLayoutParams(linLayoutParam);

        linLayout.addView(mAdView);
        return linLayout;
    }
}

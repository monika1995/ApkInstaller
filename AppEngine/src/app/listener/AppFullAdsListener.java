package app.listener;

import app.enginev4.AdsEnum;

/**
 * Created by Meenu Singh on 12/06/19.
 */
public interface AppFullAdsListener {

    void onFullAdLoaded();

    void onFullAdFailed(AdsEnum adsEnum, String errorMsg);

}

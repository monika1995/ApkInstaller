package app.campaign.request;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import app.fcm.GCMPreferences;
import app.rest.rest_utils.RestUtils;
import app.server.v2.DataHubConstant;

public class CampaignData {

    @SerializedName("app_id")
    public String app_id = DataHubConstant.APP_ID;

    @SerializedName("country")
    public String country;

    @SerializedName("screen")
    public String screen;

    @SerializedName("launchcount")
    public String launchcount;

    @SerializedName("version")
    public String version;

    @SerializedName("osversion")
    public String osversion;

    @SerializedName("dversion")
    public String dversion;

    @SerializedName("unique_id")
    public String unique_id;

    public CampaignData(Context context) {
        country = RestUtils.getCountryCode(context);
        screen = RestUtils.getScreenDimens(context);
        launchcount = RestUtils.getAppLaunchCount();
        version = RestUtils.getVersion(context);
        osversion = RestUtils.getOSVersion(context);
        dversion = RestUtils.getDeviceVersion(context);
        unique_id = new GCMPreferences(context).getUniqueId();
    }
}

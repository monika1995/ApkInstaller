package app.server.v2;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import app.rest.rest_utils.RestUtils;

/**
 * Created by Anon on 31,August,2018
 */
public class InHouseData {
    @SerializedName("app_id")
    public String appID = DataHubConstant.APP_ID;

    @SerializedName("country")
    public String country;

    @SerializedName("screen")
    public String screen;

    @SerializedName("launchcount")
    public String launchCount;

    @SerializedName("version")
    public String version;

    @SerializedName("osversion")
    public String osVersion;

    @SerializedName("type")
    public String type;

    public InHouseData(Context context, String type) {
        country = RestUtils.getCountryCode(context);
        screen = RestUtils.getScreenDimens(context);
        launchCount = RestUtils.getAppLaunchCount();
        version = RestUtils.getVersion(context);
        osVersion = RestUtils.getOSVersion(context);
        this.type = type;
    }
}

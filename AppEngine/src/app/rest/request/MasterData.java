package app.rest.request;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import app.fcm.GCMPreferences;
import app.rest.rest_utils.RestUtils;
import app.server.v2.DataHubConstant;

/**
 * Created by quantum4u1 on 23/04/18.
 */

public class MasterData {

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

    @SerializedName("unique_id")
    public String unique_id;


    public MasterData(Context context) {
        country = RestUtils.getCountryCode(context);
        screen = RestUtils.getScreenDimens(context);
        launchCount = RestUtils.getAppLaunchCount();
        version = RestUtils.getVersion(context);
        osVersion = RestUtils.getOSVersion(context);
        unique_id = new GCMPreferences(context).getUniqueId();
    }
}

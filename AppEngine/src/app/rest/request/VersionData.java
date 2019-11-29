package app.rest.request;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import app.fcm.GCMPreferences;
import app.rest.rest_utils.RestUtils;
import app.server.v2.DataHubConstant;

/**
 * Created by quantum4u1 on 24/04/18.
 */

public class VersionData {

    @SerializedName("app_id")
    public String appID = DataHubConstant.APP_ID;


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

    @SerializedName("identity")
    public String identity;

    @SerializedName("unique_id")
    public String unique_id;


    public VersionData(Context context) {
        country = RestUtils.getCountryCode(context);
        screen = RestUtils.getScreenDimens(context);
        launchcount = RestUtils.getAppLaunchCount();
        version = RestUtils.getVersion(context);
        osversion = RestUtils.getOSVersion(context);
        dversion = RestUtils.getDeviceVersion(context);
        identity = new GCMPreferences(context).getVirtualGCMID();
        unique_id = new GCMPreferences(context).getUniqueId();

    }

}

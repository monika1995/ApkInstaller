package app.rest.request;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import app.fcm.GCMPreferences;
import app.rest.rest_utils.RestUtils;
import app.server.v2.DataHubConstant;

/**
 * Created by quantum4u1 on 24/04/18.
 */

public class GCMIDData {

    @SerializedName("app_id")
    public String appID = DataHubConstant.APP_ID;

    @SerializedName("gcmid")
    public String gcmID;

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

    @SerializedName("dversion")
    public String deviceVersion;

    @SerializedName("virtual_id")
    public String virtual_id;

    @SerializedName("unique_id")
    public String unique_id;

    public GCMIDData(Context context, String mGCMId) {
        System.out.println("here is the gcm id" + " " + mGCMId);
        gcmID = mGCMId;
        country = RestUtils.getCountryCode(context);
        screen = RestUtils.getScreenDimens(context);
        launchCount = RestUtils.getAppLaunchCount();
        version = RestUtils.getVersion(context);
        osVersion = RestUtils.getOSVersion(context);
        deviceVersion = RestUtils.getDeviceVersion(context);
        virtual_id = new GCMPreferences(context).getVirtualGCMID();
        unique_id = new GCMPreferences(context).getUniqueId();
    }
}

package app.rest.request;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import app.fcm.GCMPreferences;
import app.rest.rest_utils.RestUtils;
import app.server.v2.DataHubConstant;

public class TopicsData {

    @SerializedName("app_id")
    public String appID;

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

    @SerializedName("app_topics")
    @Expose
    public ArrayList<TopicsRequest> app_topics = new ArrayList<>();

    @SerializedName("unique_id")
    public String unique_id;

    public TopicsData(Context context, ArrayList<String> list) {
        appID = DataHubConstant.APP_ID;
        country = RestUtils.getCountryCode(context);
        screen = RestUtils.getScreenDimens(context);
        launchCount = RestUtils.getAppLaunchCount();
        version = RestUtils.getVersion(context);
        osVersion = RestUtils.getOSVersion(context);
        deviceVersion = RestUtils.getDeviceVersion(context);
        unique_id = new GCMPreferences(context).getUniqueId();

        for (int i = 0; i < list.size(); i++) {
            TopicsRequest qr = new TopicsRequest(list.get(i));
            app_topics.add(qr);

        }


    }
}

package app.server.v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hp on 9/21/2017.
 */
public class DataHubPreference {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private DataHubConstant constant;

    public DataHubPreference(Context con) {
        context = con;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        constant = new DataHubConstant(context);
    }

    public String getDataHubVersion() {
        return preferences.getString("_data_hub_version_3", DataHubConstant.KEY_NA);
    }

    public void setDataHubVersion(String version) {
        editor.putString("_data_hub_version_3", version);
        editor.commit();
    }

    public void setAdsResponse(String response) {
        editor.putString("_ads_response_3", response);
        editor.commit();
    }

    public String getAdsResponse() {
        return preferences.getString("_ads_response_3", new DataHubConstant(context).parseAssetData());
    }


    public void setAdsResponseFromAsset(String response) {
        editor.putString("_ads_response_asset", response);
        editor.commit();
    }

    public String getAdsResponseFromAsset() {
        return preferences.getString("_ads_response_asset", new DataHubConstant(context).parseAssetData());
    }

    public String getAppLaunchCount() {
        String count = preferences.getString("_applaunch_count_3", "1");
        int ct = Integer.parseInt(count) + 1;
        setAppLaunchCount("" + ct);
        return count;
    }

    public void setAppLaunchCount(String count) {
        editor.putString("_applaunch_count_3", count);
        editor.commit();
    }


    public void setJSON(String json) {
        editor.putString("_json__3", json);
        editor.commit();
    }


    public String getJSON() {
        return preferences.getString("_json__3", "NA");
    }



    public void setAppName(String appName){
        editor.putString("_appName_3",appName);
        editor.commit();
    }

    public String getAppname(){
        return preferences.getString("_appName_3","");
    }

    /**
     * to saving campaign encrypted data
     */

    public void setCampaginJSON(String json) {
        editor.putString("_json_campaign_e", json);
        editor.commit();
    }


    public String getCampaignJSON() {
        return preferences.getString("_json_campaign_e", constant.readFromAssets("value.txt"));
    }

}

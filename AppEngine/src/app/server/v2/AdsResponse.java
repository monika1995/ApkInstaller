package app.server.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import app.server.v4.AdsProviders;

/**
 * Created by hp on 9/20/2017.
 */
public class AdsResponse {

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("providers")
    @Expose
    public List<AdsProviders> providers = new ArrayList<>();

    @SerializedName("provider_id")
    @Expose
    public String provider_id;

    @SerializedName("clicklink")
    @Expose
    public String clicklink;

    @SerializedName("start_date")
    @Expose
    public String start_date;

    @SerializedName("ad_id")
    @Expose
    public String ad_id;


    @SerializedName("nevigation")
    @Expose
    public String navigation;

    @SerializedName("call_native")
    @Expose
    public String call_native;

    @SerializedName("rateapptext")
    @Expose
    public String rateapptext;


    @SerializedName("rateurl")
    @Expose
    public String rateurl;


    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("updatetype")
    @Expose
    public String updatetype;


    @SerializedName("appurl")
    @Expose
    public String appurl;


    @SerializedName("prompttext")
    @Expose
    public String prompttext;


    @SerializedName("version")
    @Expose
    public String version;


    @SerializedName("moreurl")
    @Expose
    public String moreurl;


    @SerializedName("etc1")
    @Expose
    public String etc1;


    @SerializedName("etc2")
    @Expose
    public String etc2;


    @SerializedName("etc3")
    @Expose
    public String etc3;


    @SerializedName("etc4")
    @Expose
    public String etc4;


    @SerializedName("etc5")
    @Expose
    public String etc5;


    @SerializedName("src")
    @Expose
    public String src;

    @SerializedName("shareurl")
    @Expose
    public String shareurl;


    @SerializedName("sharetext")
    @Expose
    public String sharetext;

    @SerializedName("admob_banner_id")
    @Expose
    public String admob_banner_id;

    @SerializedName("admob_bannerlarge_id")
    @Expose
    public String admob_bannerlarge_id;

    @SerializedName("admob_bannerrect_id")
    @Expose
    public String admob_bannerrect_id;

    @SerializedName("admob_full_id")
    @Expose
    public String admob_full_id;


    @SerializedName("admob_native_medium_id")
    @Expose
    public String admob_native_medium_id;

    @SerializedName("admob_native_large_id")
    @Expose
    public String admob_native_large_id;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("ourapp")
    @Expose
    public String ourapp;

    @SerializedName("websitelink")
    @Expose
    public String websitelink;

    @SerializedName("ppolicy")
    @Expose
    public String ppolicy;

    @SerializedName("tandc")
    @Expose
    public String tandc;

    @SerializedName("facebook")
    @Expose
    public String facebook;

    @SerializedName("instagram")
    @Expose
    public String instagram;

    @SerializedName("twitter")
    @Expose
    public String twitter;

//    @SerializedName("adslink")
//    @Expose
//    public String adslink;

    @SerializedName("bgcolor")
    @Expose
    public String bgcolor;

    @SerializedName("textcolor")
    @Expose
    public String textcolor;

    @SerializedName("headertext")
    @Expose
    public String headertext;

    /*for exit app flow*/
    /*for non repeat exit*/
    @SerializedName("counts")
    @Expose
    public ArrayList<NonRepeatCount> counts = new ArrayList<>();

    @SerializedName("rate")
    @Expose
    public String rate;

    @SerializedName("exit")
    @Expose
    public String exit;

    @SerializedName("full")
    @Expose
    public String full;

    @SerializedName("removeads")
    @Expose
    public String removeads;

    @SerializedName("launch_counts")
    @Expose
    public ArrayList<LaunchNonRepeatCount> launch_counts = new ArrayList<>();

    @SerializedName("launch_rate")
    @Expose
    public String launch_rate;

    @SerializedName("launch_exit")
    @Expose
    public String launch_exit;

    @SerializedName("launch_full")
    @Expose
    public String launch_full;

    @SerializedName("launch_removeads")
    @Expose
    public String launch_removeads;

    /*inapp*/
    @SerializedName("app_id")
    @Expose
    public String public_key;

    @SerializedName("billing")
    @Expose
    public ArrayList<Billing> billing = new ArrayList<>();

    @SerializedName("show_ad_on_exit_prompt")
    @Expose
    public String show_ad_on_exit_prompt;


    @SerializedName("faq")
    @Expose
    public String faq;
}

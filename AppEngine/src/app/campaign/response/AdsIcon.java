package app.campaign.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 7/20/2017.
 */
public class AdsIcon {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("subtype")
    @Expose
    public String subtype;

    @SerializedName("priority")
    @Expose
    public String priority;

    @SerializedName("src")
    @Expose
    public String src;

    @SerializedName("clickurl")
    @Expose
    public String clickurl;

    @SerializedName("page_id")
    @Expose
    public String page_id;

    @SerializedName("srctext")
    @Expose
    public String srctext;

    @SerializedName("headertext")
    @Expose
    public String headertext;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("textcolor")
    @Expose
    public String textcolor;

    @SerializedName("bgcolor")
    @Expose
    public String bgcolor;

}

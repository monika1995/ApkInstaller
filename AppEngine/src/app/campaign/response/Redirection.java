package app.campaign.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 7/20/2017.
 */
public class Redirection {

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("priority")
    @Expose
    public String priority;

    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;

    @SerializedName("clickurl")
    @Expose
    public String clickurl;

    @SerializedName("is_large")
    @Expose
    public boolean is_large;

    @SerializedName("format")
    @Expose
    public String format;

    @SerializedName("header_text")
    @Expose
    public String header_text;

    @SerializedName("footer_text")
    @Expose
    public String footer_text;

    @SerializedName("header_text_color")
    @Expose
    public String header_text_color;

    @SerializedName("footer_text_color")
    @Expose
    public String footer_text_color;

    @SerializedName("button_text")
    @Expose
    public String button_text;

    @SerializedName("btton_color")
    @Expose
    public String button_color;

    @SerializedName("btton_text_color")
    @Expose
    public String button_text_color;

    @SerializedName("layout_bgcolor")
    @Expose
    public String layout_bgcolor;

    @SerializedName("pageId")
    @Expose
    public String pageId;

    @SerializedName("addId")
    @Expose
    public String addId;


}

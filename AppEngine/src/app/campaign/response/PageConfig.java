package app.campaign.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 8/10/2017.
 */
public class PageConfig {

    @SerializedName("pageName")
    @Expose
    public String pageName;

    @SerializedName("pageId")
    @Expose
    public String pageId;

    @SerializedName("adConfig")
    @Expose
    public String adConfig;
}

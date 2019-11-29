package app.server.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by quantum4u1 on 11/04/18.
 */

public class MoreFeature implements Serializable {

    @Expose(serialize = false)
    public static final String MOREFEAT_APP = "app";

    @Expose(serialize = false)
    public static final String MOREFEAT_WEB = "web";

    @SerializedName("feature_id")
    @Expose
    public String feature_id;

    @SerializedName("feature_icon")
    @Expose
    public String feature_icon;

    @SerializedName("feature_banner")
    @Expose
    public String feature_banner;

    @SerializedName("feature_text")
    @Expose
    public String feature_text;

    @SerializedName("feature_clicklink")
    @Expose
    public String feature_clicklink;

    @SerializedName("feature_type")
    @Expose
    public String feature_type;

    @SerializedName("feature_subtext")
    @Expose
    public String feature_subtext;

    @SerializedName("feature_clicktype")
    @Expose
    public String feature_clicktype;

    @SerializedName("feature_iframe")
    @Expose
    public String feature_iframe;
}

package app.server.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anon on 31,August,2018
 */
public class InHouse {

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("clicklink")
    @Expose
    public String clicklink;

    @SerializedName("src")
    @Expose
    public String src;

    @SerializedName("campType")
    @Expose
    public String campType;

    @SerializedName("html")
    @Expose
    public String html;
}

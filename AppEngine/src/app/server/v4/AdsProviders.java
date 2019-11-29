package app.server.v4;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdsProviders {

    @SerializedName("provider_id")
    @Expose
    public String provider_id;

    @SerializedName("ad_id")
    @Expose
    public String ad_id;

    @SerializedName("clicklink")
    @Expose
    public String clicklink;

    @SerializedName("src")
    @Expose
    public String src;


}

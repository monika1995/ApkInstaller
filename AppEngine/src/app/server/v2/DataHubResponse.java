package app.server.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 9/20/2017.
 */
public class DataHubResponse {

    @SerializedName("update_key")
    @Expose
    public String update_key;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("adsresponse")
    @Expose
    public List<AdsResponse> adsresponse = new ArrayList<>();

    @SerializedName("cp")
    @Expose
    public DataHubCP cp = new DataHubCP();

    @SerializedName("more_feature")
    @Expose
    public List<MoreFeature> moreFeatures = new ArrayList<>();
}

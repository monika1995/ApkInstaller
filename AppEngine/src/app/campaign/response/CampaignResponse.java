package app.campaign.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 7/20/2017.
 */
public class CampaignResponse {

    @SerializedName("response")
    @Expose
    public List<Redirection> redirection = new ArrayList<>();

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("icons")
    @Expose
    public List<AdsIcon> icons = new ArrayList<>();

    @SerializedName("pageconfig")
    @Expose
    public List<PageConfig> pageconfig = new ArrayList<>();

    @SerializedName("isStatic")
    public String isStatic;

    @SerializedName("features")
    @Expose
    public NotificationTime features = new NotificationTime();
}

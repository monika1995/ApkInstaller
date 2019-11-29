package app.fcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Meenu Singh on 2019-09-17.
 */
public class FCMTopicResponse {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("pushData")
    @Expose
    public PushData pushData = new PushData();
}

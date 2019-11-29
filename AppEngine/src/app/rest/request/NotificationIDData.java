package app.rest.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by quantum4u1 on 24/04/18.
 */

public class NotificationIDData {

    @SerializedName("reqid")
    public String reqid;

    public NotificationIDData(String reqID) {
        reqid = reqID;
    }

}

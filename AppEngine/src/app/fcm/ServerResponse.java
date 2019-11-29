package app.fcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Meenu Singh on 12-12-2017.
 */
public class ServerResponse {

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("reqvalue")
    @Expose
    public String reqvalue;
}

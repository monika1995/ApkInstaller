package app.fcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Meenu Singh on 2019-09-17.
 */
public class PushData {

    @SerializedName("reqvalue")
    @Expose
    public String reqvalue;
}

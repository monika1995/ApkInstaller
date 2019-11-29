package app.fcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Meenu Singh on 13-12-2017.
 */
public class ButtonFirst {


    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("buttontext")
    @Expose
    public String buttontext;

    @SerializedName("buttontextcolor")
    @Expose
    public String buttontextcolor;

    @SerializedName("click_type")
    @Expose
    public String click_type;

    @SerializedName("click_value")
    @Expose
    public String click_value;
}

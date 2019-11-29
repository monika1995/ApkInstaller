package app.fcm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Meenu Singh on 12-12-2017.
 */
public class NotificationUIResponse {


    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("icon_src")
    @Expose
    public String icon_src;


    @SerializedName("banner_src")
    @Expose
    public String banner_src;

    @SerializedName("headertext")
    @Expose
    public String headertext;

    @SerializedName("headertextcolor")
    @Expose
    public String headertextcolor;

    @SerializedName("footertext")
    @Expose
    public String footertext;

    @SerializedName("footertextcolor")
    @Expose
    public String footertextcolor;


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

    @SerializedName("vibration")
    @Expose
    public String vibration;

    @SerializedName("sound")
    @Expose
    public String sound;

    @SerializedName("cancelable")
    @Expose
    public String cancelable;

    @SerializedName("button1")
    @Expose
    public ButtonFirst button1 = new ButtonFirst();

    @SerializedName("button2")
    @Expose
    public ButtonSecond button2 = new ButtonSecond();

}

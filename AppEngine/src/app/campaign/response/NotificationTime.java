package app.campaign.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 9/8/2017.
 */
public class NotificationTime {

    @SerializedName("KEY_BATTERY")
    @Expose
    public String KEY_BATTERY;

    @SerializedName("KEY_RAM")
    @Expose
    public String KEY_RAM;

    @SerializedName("KEY_TEMP")
    @Expose
    public String KEY_TEMP;

    @SerializedName("KEY_IMAGEDUPLICATE")
    @Expose
    public String KEY_IMAGEDUPLICATE;

    @SerializedName("KEY_STORAGE")
    @Expose
    public String KEY_STORAGE;

    @SerializedName("KEY_CACHE")
    @Expose
    public String KEY_CACHE;

    @SerializedName("KEY_JUNK")
    @Expose
    public String KEY_JUNK;

}

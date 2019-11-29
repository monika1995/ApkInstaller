package app.server.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by quantum4u1 on 28/03/18.
 */

public class NonRepeatCount {
    @SerializedName("rate")
    @Expose
    public String rate;

    @SerializedName("exit")
    @Expose
    public String exit;

    @SerializedName("full")
    @Expose
    public String full;

    @SerializedName("removeads")
    @Expose
    public String removeads;
}

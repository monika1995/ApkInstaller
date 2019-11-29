package app.server.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by quantum4u1 on 09/04/18.
 */

public class LaunchNonRepeatCount {


    @SerializedName("launch_rate")
    @Expose
    public String launch_rate;

    @SerializedName("launch_exit")
    @Expose
    public String launch_exit;

    @SerializedName("launch_full")
    @Expose
    public String launch_full;

    @SerializedName("launch_removeads")
    @Expose
    public String launch_removeads;


}
//launch_rate
//launch_exit
//launch_full
//launch_removeads
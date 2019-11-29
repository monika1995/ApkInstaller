package app.server.v2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hp on 9/20/2017.
 */
public class DataHubCP {
    //test

    @SerializedName("cpname")
    @Expose
    public String cpname;

    @SerializedName("navigation_count")
    @Expose
    public String navigation_count;

    @SerializedName("is_start")
    @Expose
    public String is_start;

    @SerializedName("is_exit")
    @Expose
    public String is_exit;

    @SerializedName("startday")
    @Expose
    public String startday;

    @SerializedName("package_name")
    public String package_name;


    @SerializedName("camp_img")
    @Expose
    public String camp_img;

    @SerializedName("camp_click_link")
    @Expose
    public String camp_click_link;

}

package com.q4u.apkinstaller.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anon on 25,November,2019
 */
public class AppDetailsRequest {

    @SerializedName("app_name")
    public String app_name;

    @SerializedName("currentversion")
    public String currentversion;

    @SerializedName("package_name")
    public String package_name;

    public AppDetailsRequest(String appName, String pkgName, String version) {
        app_name = appName;
        currentversion = version;
        package_name = pkgName;
    }

}

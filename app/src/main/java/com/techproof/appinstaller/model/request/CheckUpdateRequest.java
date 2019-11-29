package com.techproof.appinstaller.model.request;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anon on 25,November,2019
 */
public class CheckUpdateRequest {

    @SerializedName("app_id")
    public String app_id = "qsoftwareupdate";


    @SerializedName("country")
    public String country = "in";

    @SerializedName("version")
    public String version = "1.0";

    @SerializedName("osversion")
    public String osversion = "23";

    @SerializedName("dversion")
    public String dversion = "samsung";

    @SerializedName("launchcount")
    public String launchCount = "1";

    @SerializedName("screen")
    public String screen = "xhdpi";

    @SerializedName("app_details")
    @Expose
    public ArrayList<AppDetailsRequest> app_details = new ArrayList<>();


    public CheckUpdateRequest(Context context, List<String> list) {

        for (int i = 0; i < list.size(); i++) {
            AppDetailsRequest qr = new AppDetailsRequest(appName(context, list.get(i)), list.get(i), getVersion(context, list.get(i)));
            app_details.add(qr);
        }

    }

    private String appName(Context context, String pkgName) {
        String appName;
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            appName = (String) packageManager.getApplicationLabel(info);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            appName = "";
        }
        return appName;
    }

    private String getVersion(Context context, String pkgName) {
        String appversion = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            appversion = pInfo.versionName;
            System.out.println("this is package version " + appversion);

        } catch (final PackageManager.NameNotFoundException e) {
            System.out.println(e);
        }
        return appversion;
    }
}

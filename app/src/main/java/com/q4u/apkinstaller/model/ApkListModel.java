package com.q4u.apkinstaller.model;

import android.content.pm.PackageInfo;

/**
 * Created by Anon on 21,November,2019
 */
public class ApkListModel {
    String ApkName;
    String ApkPath;
    PackageInfo packageInfo;

    public String getApkName() {
        return ApkName;
    }

    public void setApkName(String apkName) {
        ApkName = apkName;
    }

    public String getApkPath() {
        return ApkPath;
    }

    public void setApkPath(String apkPath) {
        ApkPath = apkPath;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }
}

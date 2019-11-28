package com.q4u.apkinstaller.model.response;

import com.google.gson.annotations.SerializedName;
import com.q4u.apkinstaller.model.request.AppDetailsRequest;

import java.util.List;

/**
 * Created by Anon on 25,November,2019
 */
public class CheckUpdateResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("message")
    public String message;

    @SerializedName("app_details")
    public List<AppDetailsRequest> appDetails;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AppDetailsRequest> getAppDetails() {
        return appDetails;
    }

    public void setAppDetails(List<AppDetailsRequest> appDetails) {
        this.appDetails = appDetails;
    }
}

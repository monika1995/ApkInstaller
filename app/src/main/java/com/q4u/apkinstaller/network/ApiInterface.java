package com.q4u.apkinstaller.network;

import com.q4u.apkinstaller.model.request.DataRequest;
import com.q4u.apkinstaller.model.response.DataResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Anon on 25,November,2019
 */
public interface ApiInterface {

   @POST("engine/supdate/checkupdate?engv=3")
   Call<DataResponse> checkUpdate(@Body DataRequest data);

}

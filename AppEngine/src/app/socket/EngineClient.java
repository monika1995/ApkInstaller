package app.socket;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Cache.Entry;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import app.PrintLog;
import app.ecrypt.MCrypt;
import app.fcm.GCMPreferences;
import app.rest.request.DataRequest;
import app.rest.request.GCMIDData;
import app.rest.request.MasterData;
import app.rest.request.NotificationIDData;
import app.rest.request.ReferralData;
import app.rest.request.TopicsData;
import app.rest.request.VersionData;
import app.rest.rest_utils.RestUtils;
import app.server.v2.InHouseData;

public class EngineClient {

    private int responseType = 0;
    private WeakReference<Context> weakReference;
    private Response resp;
    private GCMPreferences gcmPreferences;
    private String fcmToken, _notificatioID;
    private ArrayList<String> list;

    EngineClient(Context context, Response res) {
        weakReference = new WeakReference<>(context);
        gcmPreferences = new GCMPreferences(context);
        this.resp = res;

        if (gcmPreferences.getUniqueId().equalsIgnoreCase("NA")) {
            gcmPreferences.setUniqueId(RestUtils.generateUniqueId());
        }
    }

    void setFCMTokens(String mToken) {
        this.fcmToken = mToken;
    }

    public static final String IH_FULL = "full_ads";

    public static final String IH_LAUNCH_FULL = "launch_full_ads";
    public static final String IH_EXIT_FULL_ADS = "exit_full_ads";

    public static final String IH_CP_START = "cp_start";
    public static final String IH_CP_EXIT = "cp_exit";

    public static final String IH_TOP_BANNER = "top_banner";
    public static final String IH_BOTTOM_BANNER = "bottom_banner";
    public static final String IH_BANNER_LARGE = "banner_large";
    public static final String IH_BANNER_RECTANGLE = "banner_rectangle";

    public static final String IH_NM = "native_medium";
    public static final String IH_NL = "native_large";


    private String TYPE_INHOUSE;

    void setInHouseType(String type) {
        switch (type) {
            case IH_FULL:
                this.TYPE_INHOUSE = IH_FULL;
                break;

            case IH_LAUNCH_FULL:
                this.TYPE_INHOUSE = IH_LAUNCH_FULL;
                break;

            case IH_EXIT_FULL_ADS:
                this.TYPE_INHOUSE = IH_EXIT_FULL_ADS;
                break;

            case IH_CP_START:
                this.TYPE_INHOUSE = IH_CP_START;
                break;

            case IH_CP_EXIT:
                this.TYPE_INHOUSE = IH_CP_EXIT;
                break;

            case IH_TOP_BANNER:
                this.TYPE_INHOUSE = IH_TOP_BANNER;
                break;

            case IH_BOTTOM_BANNER:
                this.TYPE_INHOUSE = IH_BOTTOM_BANNER;
                break;

            case IH_BANNER_LARGE:
                this.TYPE_INHOUSE = IH_BANNER_LARGE;
                break;

            case IH_BANNER_RECTANGLE:
                this.TYPE_INHOUSE = IH_BANNER_RECTANGLE;
                break;

            case IH_NM:
                this.TYPE_INHOUSE = IH_NM;
                break;

            case IH_NL:
                this.TYPE_INHOUSE = IH_NL;
                break;

        }

    }

    void setNotificatioID(String _id) {
        this._notificatioID = _id;
    }

    void setAllTopics(ArrayList<String> list) {
        this.list = list;
    }

    void Communicate(String url, Object value, int responseType) {
        PrintLog.print("json check request :" + " url: " + url + " value: " + value.toString());
        this.responseType = responseType;
        try {
            RequestQueue queue = Volley.newRequestQueue(weakReference.get());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                    getJsonObject(value), createResponseListener(),
                    createErrorListener());

            if (isCacheRequestApplicable(responseType)) {
                String cache = getCachedValue(queue, jsonObjectRequest);
                PrintLog.print("65656 cache status is here " + cache
                        + " and " + responseType);
                PrintLog.print("7869 response obtained id " + cache);
                if (cache != null && cache.length() > 1)
                    resp.onResponseObtained(cache, responseType, false);
                else
                    queue.add(jsonObjectRequest);
            } else {
                queue.add(jsonObjectRequest);
                PrintLog.print("json obtained is here " + url + " a,d "
                        + jsonObjectRequest + " and ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Listener<JSONObject> createResponseListener() {

        return new Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                PrintLog.print("7869 response obtained id " + response);
                resp.onResponseObtained(response, responseType, false);

            }
        };
    }


    private ErrorListener createErrorListener() {
        return new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String jsonString = error.toString();
                PrintLog.print("response is here " + error);

                resp.onErrorObtained(jsonString, responseType);

            }
        };
    }


    private JSONObject getJsonObject(Object obj) throws JSONException {
        Gson gson = new Gson();

        if (responseType == EngineApiController.VERSION_ID_CODE) {
            VersionData vData = new VersionData(weakReference.get());
            String jsonStr = gson.toJson(vData);
            String encryptData = getEncrypterString(jsonStr);
            PrintLog.print("printing version EncryptData" + " " + jsonStr);

            ((DataRequest) obj).data = encryptData;

            String jsonObjectStr = gson.toJson(obj);
            PrintLog.print("printing version EncryptData 1" + " " + jsonObjectStr);

            return new JSONObject(jsonObjectStr);
        } else if (responseType == EngineApiController.MASTER_SERVICE_CODE) {
            MasterData mData = new MasterData(weakReference.get());
            String jsonStr = gson.toJson(mData);

            String encryptData = getEncrypterString(jsonStr);

            PrintLog.print("printing master EncryptData" + " " + jsonStr);

            ((DataRequest) obj).data = encryptData;

            String jsonObjectStr = gson.toJson(obj);
            PrintLog.print("printing master EncryptData 1" + " " + jsonObjectStr);

            return new JSONObject(jsonObjectStr);
        } else if (responseType == EngineApiController.GCM_SERVICE_CODE) {

            GCMIDData gData = new GCMIDData(weakReference.get(), fcmToken);
            String jsonStr = gson.toJson(gData);
            String encryptData = getEncrypterString(jsonStr);
            PrintLog.print("printing gcm EncryptData from service" + " " + jsonStr);

            ((DataRequest) obj).data = encryptData;

            String jsonObjectStr = gson.toJson(obj);
            PrintLog.print("printing gcm EncryptData from service 1" + " " + jsonObjectStr);

            return new JSONObject(jsonObjectStr);

        } else if (responseType == EngineApiController.NOTIFICATION_ID_CODE) {

            NotificationIDData notiData = new NotificationIDData(_notificatioID);
            String jsonStr = gson.toJson(notiData);
            String encryptData = getEncrypterString(jsonStr);
            PrintLog.print("printing notification EncryptData " + " " + jsonStr);

            ((DataRequest) obj).data = encryptData;

            String jsonObjectStr = gson.toJson(obj);
            PrintLog.print("printing notification Encryption 1" + " " + jsonObjectStr);

            return new JSONObject(jsonObjectStr);
        } else if (responseType == EngineApiController.REFERRAL_ID_CODE) {
            ReferralData rData = new ReferralData(weakReference.get(), gcmPreferences.getreferrerId());
            String jsonStr = gson.toJson(rData);


            ((DataRequest) obj).data = getEncrypterString(jsonStr);

            String jsonObjectStr = gson.toJson(obj);
            PrintLog.print("printing referal from 1" + " " + jsonObjectStr);
            return new JSONObject(jsonObjectStr);
        } else if (responseType == EngineApiController.INHOUSE_CODE) {
            InHouseData iData = new InHouseData(weakReference.get(), TYPE_INHOUSE);

            String jsonStr = gson.toJson(iData);

            ((DataRequest) obj).data = getEncrypterString(jsonStr);

            String jsonObject = gson.toJson(obj);
            PrintLog.print("printing INHOUSE from 1" + " " + jsonObject);
            return new JSONObject(jsonObject);

        } else if (responseType == EngineApiController.FCM_TOPIC_CODE) {
            TopicsData iData = new TopicsData(weakReference.get(), list);

            String jsonStr = gson.toJson(iData);

            ((DataRequest) obj).data = getEncrypterString(jsonStr);

            String jsonObject = gson.toJson(obj);
            PrintLog.print("printing FCM_TOPIC_CODE " + " " + jsonObject);
            return new JSONObject(jsonObject);
        }

        return null;
    }

    private String getEncrypterString(String jsonStr) {
        String value = "";
        MCrypt mcrypt = new MCrypt();
        try {
            value = MCrypt.bytesToHex(mcrypt.encrypt(jsonStr));
        } catch (Exception e) {
            PrintLog.print("exception encryption" + " " + e);
            e.printStackTrace();
        }
        return value;
    }

    public static String loadJSONFromAsset(Context context, String pathName) {
        PrintLog.print("45845 json retun is here" + pathName);
        String json;
        try {
            PrintLog.print("json retun is here" + pathName);
            InputStream is = context.getAssets().open(pathName + ".json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, StandardCharsets.UTF_8);

            PrintLog.print("json retun is here" + json.length());
        } catch (IOException ex) {
            ex.printStackTrace();
            PrintLog.print("json retun is here" + ex);
            return null;
        }
        return json;

    }

    private String getCachedValue(RequestQueue queue, JsonObjectRequest request) {
        Entry entry = queue.getCache().get(request.getCacheKey());
        PrintLog.print("78989 status of cache entry is here "
                + request.getCacheKey());

        if (entry != null && entry.data != null)
            return new String(entry.data);
        else
            return null;

    }

    private boolean isCacheRequestApplicable(int responeType) {
        // if (responeType == EngineApiController.ROOTPAGEDETAIL
        // || responseType == EngineApiController.NEXTPAGEDETAIL)
        // return true;
        // else
        return false;

    }
}

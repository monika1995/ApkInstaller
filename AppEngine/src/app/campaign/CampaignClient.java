package app.campaign;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import app.PrintLog;
import app.campaign.request.CampaignData;
import app.campaign.request.CampaignDataRequest;
import app.ecrypt.MCrypt;
import app.fcm.GCMPreferences;
import app.rest.rest_utils.RestUtils;


public class CampaignClient {

    private int responseType = 0;
    private WeakReference<Context> weakReference;
    private ICampaignResponseCallback resp;

    CampaignClient(Context context, ICampaignResponseCallback res) {
        weakReference = new WeakReference<Context>(context);
        this.resp = res;
        GCMPreferences gcmPreferences = new GCMPreferences(context);

        if (gcmPreferences.getUniqueId().equalsIgnoreCase("NA")) {
            gcmPreferences.setUniqueId(RestUtils.generateUniqueId());
        }
    }


    void Communicate(String url, Object value, int responseType) {
        PrintLog.print("json check campaign  :" + " url: " + url + " value: " + value.toString());
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

    private com.android.volley.Response.Listener<JSONObject> createResponseListener() {

        return new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                PrintLog.print("7869 response obtained id " + response);
                resp.onResponseObtained(response, responseType, false);

            }
        };
    }


    private com.android.volley.Response.ErrorListener createErrorListener() {
        return new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String jsonString = "";
                PrintLog.print("response is here " + error);

                resp.onErrorObtained(jsonString, responseType);

            }
        };
    }


    private JSONObject getJsonObject(Object obj) throws JSONException {
        Gson gson = new Gson();

        if (responseType == CampaignApiController.CAMPAIGN_SERVICE_CODE) {
            CampaignData vData = new CampaignData(weakReference.get());
            String jsonStr = gson.toJson(vData);
            String encryptData = getEncrypterString(jsonStr);
            PrintLog.print("printing campaign EncryptData" + " " + encryptData);

            ((CampaignDataRequest) obj).data = encryptData;

            String jsonObjectStr = gson.toJson(obj);
            PrintLog.print("printing campaign EncryptData 1" + " " + jsonObjectStr);

            return new JSONObject(jsonObjectStr);
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

            json = new String(buffer, "UTF-8");

            PrintLog.print("json retun is here" + json.length());
        } catch (IOException ex) {
            ex.printStackTrace();
            PrintLog.print("json retun is here" + ex);
            return null;
        }
        return json;

    }

    private String getCachedValue(RequestQueue queue, JsonObjectRequest request) {
        Cache.Entry entry = queue.getCache().get(request.getCacheKey());
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

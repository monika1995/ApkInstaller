package app.fcm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import app.PrintLog;

/**
 * Created by Meenu Singh on 05-12-2017.
 */
public class GCMUtils {

    public static final String sendDataURL = "http://quantum4you.com/engine/gcm/requestgcm?gcmid=";
    public static final String receiveDataURL = "http://quantum4you.com/engine/gcm/requestnotification?reqid=";


    public String getDeviceName() {

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        else {
            return capitalize(manufacturer);
        }
    }

    public String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String getDeviceId(Context ctx) {
        String deviceId = "";
        deviceId = Settings.Secure.getString(ctx.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }

//    public String getDeviceId(Context ctx) {
//        String deviceId = "";
//        final TelephonyManager mTelephony = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
//        if (mTelephony.getDeviceId() != null) {
//            deviceId = mTelephony.getDeviceId();
//        } else {
//            deviceId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
//        }
//        return deviceId;
//    }

    public static int getAppVersionCode(Context mContext){
        try {
            PackageInfo _info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return _info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }

    //In this function you register your app for GCM notification.
    public static String registerYourApp(Activity activity, Context context, String APP_ID) {
        return APP_ID;
    }


    public static String performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) {
        URL url = null;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;

                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintLog.print("RegistrationIntentService.performPostCall " + response + url);
        return response;
    }

    public static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        PrintLog.print("RegistrationIntentService.getPostDataString " + result.toString());

        return result.toString();
    }

}

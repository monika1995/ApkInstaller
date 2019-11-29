package app.fcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Meenu Singh on 29-11-2017.
 */
public class GCMPreferences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    //  public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    private static final String KEY_TOKEN = "token_3";
    private static final String KEY_DEVICE_NAME = "key_device_name_3";
    private static final String KEY_ANDROID_VERSION = "key_android_version_3";
    private static final String KEY_COUNTRY = "key_country_3";
    private static final String KEY_REG_APP = "key_reg_app_3";
    private static final String KEY_DEVICE_ID = "key_device_id_3";
    private static final String KEY_TOKEN_VALUE = "key_token_value_3";
    private static final String KEY_APP_VERSION = "key_app_version_3";
    private static final String KEY_ReferrerId = "key_referrerId_3";
    private static final String KEY_GCMTOKEN = "key_gcm_token_3";
    private static final String KEY_TOPIC_APP_VER = "key_topic_app_ver";
    private static final String KEY_UNIQUE_ID = "key_unique_id";
    private static final String KEY_FCM_NOTI_ID = "key_fcm_noti_id";
    private static final String KEY_OnBoard_NOTI_ID = "key_onboard_noti_id";
    private static final String KEY_FCM_RANDOM_DELAY = "key_fcm_random_delay";
    private static final String KEY_FCM_RANDOM_ONBOARD = "key_fcm_random_onboard";

    public GCMPreferences(Context con) {
        context = con;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }


    public String getGCMID() {
        return preferences.getString("_real_gcm_id_3", "NA");
    }

    public void setGCMID(String value) {
        editor.putString("_real_gcm_id_3", value);
        editor.commit();
    }


    public String getVirtualGCMID() {
        return preferences.getString(KEY_TOKEN, "NA");
    }

    public void setVirtualGCMID(String value) {
        editor.putString(KEY_TOKEN, value);
        editor.commit();
    }

    public String getDeviceName() {
        return preferences.getString(KEY_DEVICE_NAME, " ");
    }

    public void setDeviceName(String value) {
        editor.putString(KEY_DEVICE_NAME, value);
        editor.commit();
    }

    public String getAndroidVersion() {
        return preferences.getString(KEY_ANDROID_VERSION, " ");
    }

    public void setAndroidVersion(String value) {
        editor.putString(KEY_ANDROID_VERSION, value);
        editor.commit();
    }

    public String getCountry() {
        return preferences.getString(KEY_COUNTRY, " ");
    }

    public void setCountry(String value) {
        editor.putString(KEY_COUNTRY, value);
        editor.commit();
    }

    public String getDeviceId() {
        return preferences.getString(KEY_DEVICE_ID, " ");
    }

    public void setDeviceId(String value) {
        editor.putString(KEY_DEVICE_ID, value);
        editor.commit();
    }


    public int getAppVersion() {
        return preferences.getInt(KEY_APP_VERSION, 0);
    }

    public void setAppVersion(int value) {
        editor.putInt(KEY_APP_VERSION, value);
        editor.commit();
    }

    public String getregisterYourApp() {
        return preferences.getString(KEY_REG_APP, " ");
    }

    public void setregisterYourApp(String value) {
        editor.putString(KEY_REG_APP, value);
        editor.commit();
    }

    public String getreferrerId() {
        return preferences.getString(KEY_ReferrerId, "NA");
    }

    public void setreferrerId(String value) {
        editor.putString(KEY_ReferrerId, value);
        editor.commit();
    }

    public Boolean getGCMRegister() {
        return preferences.getBoolean("_gcm_registration_3", false);
    }

    public void setGCMRegister(Boolean value) {
        editor.putBoolean("_gcm_registration_3", value);
        editor.commit();
    }

    public Boolean getReferalRegister() {
        return preferences.getBoolean("_referal_register_3", false);
    }

    public void setReferalRegister(Boolean value) {
        editor.putBoolean("_referal_register_3", value);
        editor.commit();
    }

    public void setFirstTime(boolean flag) {
        editor.putBoolean("splash_pref", flag);
        editor.commit();
    }

    public boolean isFirsttime() {
        return preferences.getBoolean("splash_pref", true);
    }

    public void setFirstTimenoti(boolean flag) {
        editor.putBoolean("notifirst", flag);
        editor.commit();
    }

    public boolean isFirsttimenoti() {
        return preferences.getBoolean("notifirst", false);
    }

    public boolean getregisterAllTopics() {
        return preferences.getBoolean("_register_all_topics", false);
    }

    public void setregisterAllTopics(Boolean value) {
        editor.putBoolean("_register_all_topics", value);
        editor.commit();
    }

    public boolean getAllSubscribeTopic() {
        return preferences.getBoolean("allsubscribeTopic", false);
    }

    public void setAllSubscribeTopic(Boolean value) {
        editor.putBoolean("allsubscribeTopic", value);
        editor.commit();
    }

    public String getTopicAppVersion() {
        return preferences.getString(KEY_TOPIC_APP_VER, "");
    }

    public void setTopicAppVersion(String value) {
        editor.putString(KEY_TOPIC_APP_VER, value);
        editor.commit();
    }

    public String getUniqueId() {
        return preferences.getString(KEY_UNIQUE_ID, "NA");
    }

    public void setUniqueId(String value) {
        editor.putString(KEY_UNIQUE_ID, value);
        editor.commit();
    }

    public String getFCMNotificationId() {
        return preferences.getString(KEY_FCM_NOTI_ID, "");
    }

    public void setFCMNotificationId(String value) {
        editor.putString(KEY_FCM_NOTI_ID, value);
        editor.commit();
    }

    public String getOnBoardNotificationId() {
        return preferences.getString(KEY_OnBoard_NOTI_ID, "");
    }

    public void setOnBoardNotificationId(String value) {
        editor.putString(KEY_OnBoard_NOTI_ID, value);
        editor.commit();
    }

    public int getFCMRandomDelay() {
        return preferences.getInt(KEY_FCM_RANDOM_DELAY, 0);
    }

    public void setFCMRandomDelay(int value) {
        editor.putInt(KEY_FCM_RANDOM_DELAY, value);
        editor.commit();
    }


    public int getFCMRandomOnboard() {
        return preferences.getInt(KEY_FCM_RANDOM_ONBOARD, 0);
    }

    public void setFCMRandomOnboard(int value) {
        editor.putInt(KEY_FCM_RANDOM_ONBOARD, value);
        editor.commit();
    }
}
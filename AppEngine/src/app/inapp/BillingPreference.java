package app.inapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by quantum4u1 on 18/04/18.
 */

public class BillingPreference {


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public BillingPreference(Context con) {
        preferences = PreferenceManager.getDefaultSharedPreferences(con);
        editor = preferences.edit();
    }

    public boolean isPro() {
        return preferences.getBoolean("is_premium", false);
    }

    public void setPro(boolean value) {
        editor.putBoolean("is_premium", value);
        editor.commit();
    }


    public boolean isWeekly() {
        return preferences.getBoolean("is_weekly", false);
    }

    public void setWeekly(boolean value) {
        editor.putBoolean("is_weekly", value);
        editor.commit();
    }

    public boolean isMonthly() {
        return preferences.getBoolean("is_monthly", false);
    }

    public void setMonthly(boolean value) {
        editor.putBoolean("is_monthly", value);
        editor.commit();
    }


    public boolean isYearly() {
        return preferences.getBoolean("is_yearly", false);
    }

    public void setYearly(boolean value) {
        editor.putBoolean("is_yearly", value);
        editor.commit();
    }


    public boolean isHalfYearly() {
        return preferences.getBoolean("is_halfyearly", false);
    }

    public void setHalfYearly(boolean value) {
        editor.putBoolean("is_halfyearly", value);
        editor.commit();
    }
}

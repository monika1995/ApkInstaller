package app.rest.rest_utils;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import app.fcm.GCMPreferences;
import app.server.v2.DataHubConstant;

/**
 * Created by quantum4u1 on 23/04/18.
 */

public class RestUtils {


    public static String getCountryCode(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        assert tm != null;
        return tm.getSimCountryIso();
        //return "fr";

    }

    public static String getScreenDimens(Context context) {
        String Screen = "hdpi";
        try {
            float den = context.getResources().getDisplayMetrics().density;
            if (den == .75) {
                Screen = "LDPI";
            } else if (den == 1.0) {
                Screen = "MDPI";
            } else if (den == 1.5) {
                Screen = "HDPI";
            } else if (den == 2.0) {
                Screen = "XHDPI";
            } else if (den == 3.0) {
                Screen = "XXHDPI";
            } else if (den == 4.0) {
                Screen = "XXXHDPI";
            }


        } catch (Exception e) {
            return Screen;
        }
        return Screen;
    }

    public static String getAppLaunchCount() {
        return Integer.toString(DataHubConstant.APP_LAUNCH_COUNT);
    }

    public static String getVersion(Context context) {
        String appversion = "1";
        try {
            appversion = "" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {

        }
        return appversion;
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public static String getOSVersion(Context context) {
        return "" + Build.VERSION.SDK_INT;
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public static String getDeviceVersion(Context context) {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.contains(" ") || manufacturer.contains(" ")) {
            model = model.replace(" ", "%20");
            manufacturer = manufacturer.replace(" ", "%20");

        }
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer);
        }
    }

    private static String capitalize(String s) {
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


    public static boolean isVirtual(Context ctx) {
        GCMPreferences preferences = new GCMPreferences(ctx);

        if (getVersion(ctx).equalsIgnoreCase(String.valueOf(preferences.getAppVersion()))
                && getOSVersion(ctx).equalsIgnoreCase(preferences.getAndroidVersion())
                && getDeviceVersion(ctx).equalsIgnoreCase(preferences.getDeviceName())
                && getCountryCode(ctx).equalsIgnoreCase(preferences.getCountry())
                && DataHubConstant.APP_ID.equalsIgnoreCase(preferences.getregisterYourApp())) {


            return false;
        } else {
            return true;
        }

    }


    public static void saveAllGCMValue(Context ctx) {
        GCMPreferences preferences = new GCMPreferences(ctx);

        preferences.setregisterYourApp(DataHubConstant.APP_ID);
        preferences.setDeviceName(getDeviceVersion(ctx));
        preferences.setAndroidVersion(getOSVersion(ctx));
        preferences.setCountry(getCountryCode(ctx));
        preferences.setAppVersion(Integer.parseInt(getVersion(ctx)));
    }

    /* public static String getTimeofLos_Angeles() {
         Calendar calendar = Calendar.getInstance();
         SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy_hh");
         sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
         return sdf.format(calendar.getTime());
     }
 */
    public static String getDateofLos_Angeles() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return sdf.format(calendar.getTime());
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("dd-MMM-yyyy", cal).toString();
    }

    public static String getMonthofLos_Angeles() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        return sdf.format(calendar.getTime());
    }


    public static boolean validateJavaDate(String strDate) {
        /* Check if date is 'null' */
        if (strDate.trim().equals("")) {
            return true;
        }
        /* Date is not 'null' */
        else {
            /*
             * Set preferred date format,
             * For example MM-dd-yyyy, MM.dd.yyyy,dd.MM.yyyy etc.*/
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd-MMM-yyyy");
            sdfrmt.setLenient(false);
            /* Create Date object
             * parse the string into date
             */
            try {
                Date javaDate = sdfrmt.parse(strDate);
                System.out.println("validateJavaDate " + strDate + " is valid date format");
            }
            /* Date format is invalid */ catch (ParseException e) {
                System.out.println("validateJavaDate " + strDate + " is Invalid Date format");
                return false;
            }
            /* Return true if date format is valid */
            return true;
        }
    }

    public static String generateUniqueId() {
        String unique_id = "" + System.currentTimeMillis() + getRandomNo();
        System.out.println("RestUtils.generateUniqueId " + unique_id.length());
        return unique_id;
    }


    private static int getRandomNo() {
        Random r = new Random();
        int low = 10000;
        int high = 99000;
        return r.nextInt(high - low) + low;
    }
}

package app.utils;

/*
 * This class is wrapper over the Log for Android. This has been seperated
 * to attain the logging in one place and to achieve test result
 */


import android.util.Log;

import app.pnd.adshandler.BuildConfig;


public class DebugLogger {

    private static boolean LOGGING = BuildConfig.DEBUG;

    /**
     * Logging debug type logs and writing into text file
     *
     * @param TAG
     * @param logMsg
     */
    public static void d(String TAG, String logMsg) {
        if (LOGGING) {
            Log.d(TAG, logMsg);
        }
    }

    /**
     * Logging verbose type logs and writing into text file
     *
     * @param TAG
     * @param logMsg
     */
    public static void v(String TAG, String logMsg) {
        if (LOGGING) {
            Log.v(TAG, logMsg);
        }
    }

    /**
     * Logging info type logs and writing into text file
     *
     * @param TAG
     * @param logMsg
     */

    public static void i(String TAG, String logMsg) {
        if (LOGGING) {
            Log.i(TAG, logMsg);
        }
    }


    /**
     * Logging error type logs and writing into text file
     *
     * @param TAG
     * @param logMsg
     */
    public static void e(String TAG, String logMsg) {
        if (LOGGING) {
            Log.e(TAG, logMsg);
        }
    }

    /**
     * Logging warning type logs and writing into text file
     *
     * @param TAG
     * @param logMsg
     */
    public static void w(String TAG, String logMsg) {
        if (LOGGING) {
            Log.w(TAG, logMsg);
        }
    }

}

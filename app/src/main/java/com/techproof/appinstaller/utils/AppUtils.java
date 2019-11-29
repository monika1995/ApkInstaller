package com.techproof.appinstaller.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Anon on 29,November,2019
 */
public class AppUtils {

    public static void onClickButtonFirebaseAnalytics(Context context, String bundleName) {

        Bundle bundle = new Bundle();
        bundle.putString(bundleName, bundleName);
        FirebaseAnalytics firebaseAnalytics = getFirebaseAnalytics(context);
        firebaseAnalytics.logEvent(bundleName, bundle);
    }

    public static void onClickButtonFirebaseAnalytics(Context context, String bundleName, String bundleDes) {

        Bundle bundle = new Bundle();
        bundle.putString(bundleName, bundleDes);
        FirebaseAnalytics firebaseAnalytics = getFirebaseAnalytics(context);
        firebaseAnalytics.logEvent(bundleName, bundle);
    }
    private static FirebaseAnalytics getFirebaseAnalytics(Context context) {
        return FirebaseAnalytics.getInstance(context);
    }
}

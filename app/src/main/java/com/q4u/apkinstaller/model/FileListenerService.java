package com.q4u.apkinstaller.model;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import com.q4u.apkinstaller.utils.RecursiveFileObserver;

/**
 * Created by Anon on 27,November,2019
 */
public class FileListenerService extends Service {

    RecursiveFileObserver fileObserver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, new Notification());
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileObserver = new RecursiveFileObserver(getPackageName(),getApplicationContext(),path);
        fileObserver.startWatching();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fileObserver.stopWatching();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }
    }
}

package com.techproof.appinstaller.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.techproof.appinstaller.R;
import com.techproof.appinstaller.utils.RecursiveFileObserver;

/**
 * Created by Anon on 27,November,2019
 */
public class FileListenerService extends Service {

    RecursiveFileObserver fileObserver;
    NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
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

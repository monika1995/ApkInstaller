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

        createChannel();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileObserver = new RecursiveFileObserver(getPackageName(),getApplicationContext(),path);
        fileObserver.startWatching();


    }

    public void createChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelId);
            notificationChannel.setSound(null, null);

            notificationManager.createNotificationChannel(notificationChannel);
            Notification notification = new Notification.Builder(this, channelId)
                    .setPriority(Notification.PRIORITY_MIN)
                    .build();

            startForeground(1, notification);
        }
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

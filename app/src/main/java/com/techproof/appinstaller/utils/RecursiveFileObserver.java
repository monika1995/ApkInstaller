package com.techproof.appinstaller.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.FileObserver;
import android.os.Handler;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.techproof.appinstaller.Common.BaseClass;
import com.techproof.appinstaller.Common.Constant;
import com.techproof.appinstaller.Common.DebugLogger;
import com.techproof.appinstaller.R;
import com.techproof.appinstaller.activity.SplashActivity;
import com.techproof.appinstaller.activity.SplashActivityV3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Anon on 27,November,2019
 */
public class RecursiveFileObserver extends FileObserver
{
        /** Only modification events */
        public static int CHANGES_ONLY = CREATE | DELETE | CLOSE_WRITE | MOVE_SELF
                | MOVED_FROM | MOVED_TO;

        private List<SingleFileObserver> mObservers;
        private String mPath;
        private int mMask;
        private Context context;
        private Intent intent;
        private String packageName;
        LocalBroadcastManager manager;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        boolean isNotification;
        BaseClass baseClass;
        private static RemoteViews contentView;
        NotificationChannel notificationChannel = null;
        NotificationManager notificationManager;
        String ANDROID_CHANNEL_ID = "notify001";
        Notification notification;
        NotificationCompat.Builder builder;
        PackageInfo packageInfo;

        public RecursiveFileObserver(String packageName,Context context,String path)
        {
            this(packageName,context,path, CHANGES_ONLY);
        }

        public RecursiveFileObserver(String packageName,Context context,String path, int mask)
        {
            super(path, mask);
            this.context = context;
            this.packageName = packageName;
            mPath = path;
            mMask = mask;
            manager = LocalBroadcastManager.getInstance(context);
            sharedPreferences = context.getSharedPreferences(Constant.PREF_NAME, Context.MODE_PRIVATE);
            editor = context.getSharedPreferences("My_Pref", 0).edit();
            baseClass =  new BaseClass();
        }

        @Override
        public void startWatching()
        {
            if (mObservers != null)
                return ;

            mObservers = new ArrayList();
            Stack stack = new Stack();
            stack.push(mPath);

            while (!stack.isEmpty())
            {
                String parent = String.valueOf(stack.pop());
                mObservers.add(new SingleFileObserver(parent, mMask));
                File path = new File(parent);
                File[]files = path.listFiles();
                if (null == files)
                    continue;
                for (File f: files)
                {
                    if (f.isDirectory() && !f.getName().equals(".") && !f.getName()
                            .equals(".."))
                    {
                        stack.push(f.getPath());
                    }
                }
            }

            for (SingleFileObserver sfo: mObservers)
            {
                sfo.startWatching();
            }
        }

        @Override
        public void stopWatching()
        {
            if (mObservers == null)
                return ;


            for (SingleFileObserver sfo: mObservers)
            {
                sfo.stopWatching();
            }

            mObservers.clear();
            mObservers = null;
        }

        @Override
        public void onEvent(int event, String path)
        {
            switch (event)
            {
                case FileObserver.CREATE:
                    if (path.contains(".apk")) {
                        DebugLogger.d("RecursiveFileObserver CREATE: " + path);
                        isNotification = sharedPreferences.getBoolean(Constant.IS_NOTIFICATION, true);
                        if (isNotification) {
                            intent = new Intent("APK file create");
                            intent.putExtra(Constant.PATH, path);
                            manager.sendBroadcast(intent);
                            editor.putString(Constant.APK_PATH, path);
                            editor.commit();
                            DebugLogger.d("isNotification " + isNotification);
                            createNotification(context, path);
                        }
                    }
                    break;
                case FileObserver.MODIFY:
                case FileObserver.MOVED_TO:
                case FileObserver.MOVED_FROM:
                case FileObserver.MOVE_SELF:
                    DebugLogger.d("RecursiveFileObserver Modify: " + path);
                    break;
                case FileObserver.DELETE:
                case FileObserver.DELETE_SELF:
                    intent = new Intent("APK file create");
                    intent.putExtra(Constant.PATH, path);
                    manager.sendBroadcast(intent);
                    DebugLogger.d("RecursiveFileObserver DELETE_SELF: " + path);
                    break;
            }
        }

    /**
     * Monitor single directory and dispatch all events to its parent, with full path.
     */
    class SingleFileObserver extends FileObserver
    {
        String mPath;

        public SingleFileObserver(String path)
        {
            this(path, ALL_EVENTS);
            mPath = path;
        }

        public SingleFileObserver(String path, int mask)
        {
            super(path, mask);
            mPath = path;
        }

        @Override public void onEvent(int event, String path)
        {
            String newPath = mPath + "/" + path;
            RecursiveFileObserver.this.onEvent(event, newPath);
        }
    }

    public void createNotification(Context context,String path) {
       // PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(path, 0);

        //if (packageInfo != null) {

            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID);

            DebugLogger.d("packinfo_noti" + packageInfo);

            contentView = new RemoteViews(packageName, R.layout.layout_notification);
            Intent switchIntent = new Intent(context, SplashActivityV3.class);
            switchIntent.putExtra(Constant.FROM_NOTIFICATION, true);
            switchIntent.putExtra(Constant.PATH, path);
            PendingIntent pendingSwitchIntent = PendingIntent.getActivity(context, 0, switchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //contentView.setOnClickPendingIntent(R.id.btn_notifi_install, pendingSwitchIntent);
            contentView.setImageViewResource(R.id.img_noti_app, R.drawable.ic_android);

            builder.setSmallIcon(R.drawable.status_app_icon);
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_DEFAULT);
            builder.setContent(contentView);
            builder.setContentIntent(pendingSwitchIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(ANDROID_CHANNEL_ID, "channel name", NotificationManager.IMPORTANCE_HIGH);
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(ANDROID_CHANNEL_ID);
            }

            notification = builder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(new Random().nextInt(), notification);
        //}
    }
}

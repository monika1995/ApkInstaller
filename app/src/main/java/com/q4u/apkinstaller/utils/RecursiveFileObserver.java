package com.q4u.apkinstaller.utils;

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
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.q4u.apkinstaller.Common.BaseClass;
import com.q4u.apkinstaller.Common.Constant;
import com.q4u.apkinstaller.Common.DebugLogger;
import com.q4u.apkinstaller.R;
import com.q4u.apkinstaller.activity.SplashActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Anon on 27,November,2019
 */
public class RecursiveFileObserver extends FileObserver
{
        /** Only modification events */
        public static int CHANGES_ONLY = CREATE | DELETE | CLOSE_WRITE | MOVE_SELF
                | MOVED_FROM | MOVED_TO;

        List<SingleFileObserver> mObservers;
        String mPath;
        int mMask;
        Context context;
        Intent intent;
        String packageName;
        LocalBroadcastManager manager;
        SharedPreferences sharedPreferences;
        boolean isNotification;

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
                    DebugLogger.d("RecursiveFileObserver CREATE: " + path);
                    intent = new Intent("APK file create");
                    intent.putExtra(Constant.PATH, path);
                    manager.sendBroadcast(intent);
                    isNotification = sharedPreferences.getBoolean(Constant.IS_NOTIFICATION,true);
                    DebugLogger.d("isNotification "+ isNotification);
                    if(isNotification)
                      new BaseClass().createNotification(context,path);
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
}

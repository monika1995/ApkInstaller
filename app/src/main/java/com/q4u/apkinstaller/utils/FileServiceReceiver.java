package com.q4u.apkinstaller.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.q4u.apkinstaller.model.FileListenerService;

/**
 * Created by Anon on 28,November,2019
 */
public class FileServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, FileListenerService.class));
        }else {
            context.startService(new Intent(context, FileListenerService.class));
        }
    }
}

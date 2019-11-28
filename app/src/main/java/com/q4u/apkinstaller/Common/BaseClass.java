package com.q4u.apkinstaller.Common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.jaredrummler.apkparser.ApkParser;
import com.jaredrummler.apkparser.model.AndroidManifest;
import com.q4u.apkinstaller.R;
import com.q4u.apkinstaller.activity.SplashActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Anon on 27,November,2019
 */
public class BaseClass {

    private static RemoteViews contentView;
    NotificationChannel notificationChannel = null;
    NotificationManager notificationManager;
    String ANDROID_CHANNEL_ID = "com.q4u.apkinstaller.ANDROID";
    String packageName;
    Notification notification;
    NotificationCompat.Builder builder;

    public List<String> getListOfPermissions(final Context context, String filePath) {

        List<String> list = new ArrayList<>();
        File file = new File(filePath);
        if(file.exists()) {
            ApkParser apkParser = ApkParser.create(file);
            try {
                AndroidManifest manifest = apkParser.getAndroidManifest();
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(new InputSource(new StringReader(manifest.xml)));
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("uses-permission");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        list.add(eElement.getAttribute("android:name"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void createNotification(Context context,String path)
    {
        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(path, 0);

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context, ANDROID_CHANNEL_ID);

        contentView = new RemoteViews(packageName, R.layout.layout_notification);
        if(packageInfo!=null) {
            Drawable icon = context.getPackageManager().getApplicationIcon(packageInfo.applicationInfo);
            Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
            contentView.setImageViewBitmap(R.id.img_noti_app, bitmap);
        }
        Intent switchIntent = new Intent(context, SplashActivity.class);
        switchIntent.putExtra(Constant.FROM_NOTIFICATION,true);
        switchIntent.putExtra(Constant.PATH,path);
        PendingIntent pendingSwitchIntent = PendingIntent.getActivity(context, 0, switchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.btn_notifi_install, pendingSwitchIntent);
        contentView.setImageViewResource(R.id.img_noti_app,R.mipmap.ic_launcher);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        builder.setContent(contentView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ANDROID_CHANNEL_ID, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(ANDROID_CHANNEL_ID);
        }

        notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(101, notification);
    }

}

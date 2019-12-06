package app.fcm.fcmlistener;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import app.fcm.ButtonFirst;
import app.fcm.ButtonSecond;
import app.fcm.MapperUtils;
import app.fcm.NotificationActionReceiver;
import app.fcm.NotificationUIResponse;
import app.fcm.imageparser.ImageDownloader;
import app.fcm.imageparser.LoadImage;
import app.pnd.adshandler.R;
import app.server.v2.DataHubConstant;

/**
 * Created by quantum4u1 on 27/04/18.
 */

public class Type3PushListener implements FCMType, ImageDownloader {

    private NotificationUIResponse push;
    private Context context;
    private String CHANNEL_NAME;
    private String NOTIFICATION_CHANNEL_DISCRIPTION;

    @Override
    public void generatePush(Context c, NotificationUIResponse r) {
        if (r != null) {

            this.push = r;
            this.context = c;

            CHANNEL_NAME = new DataHubConstant(this.context).notificationChannelName();
            NOTIFICATION_CHANNEL_DISCRIPTION = CHANNEL_NAME + " Push Notification";

            if (r.banner_src != null
                    && !r.banner_src.equalsIgnoreCase("NA")
                    && !r.banner_src.equalsIgnoreCase("")) {

                new LoadImage(c, getAllUrlstobeDOwnloadedFromPush(r), this).startDownload();
            }


        }
    }

    private void createNotification(Map<String, Bitmap> mBitmap, NotificationUIResponse push) {
        if (push.button1 != null) {
            ButtonFirst buttonValue = new ButtonFirst();
            buttonValue = push.button1;
            if (push.button1 != null || push.button2 != null) {
                ButtonFirst buttonValue1 = new ButtonFirst();
                buttonValue1 = push.button1;
                ButtonSecond buttonValue2 = new ButtonSecond();
                buttonValue2 = push.button2;
                try {
                    if (buttonValue2.status.equalsIgnoreCase("0")) {
                        type4Notification(mBitmap, buttonValue1, buttonValue2);

                    } else if (buttonValue.status.equalsIgnoreCase("0")) {
                        type3Notification(mBitmap, buttonValue1);
                    }

                } catch (Exception e) {
                    System.out.println("Type3PushListener.createNotification " + e.getMessage());
                }

            }

        }

    }

    private void type3Notification(Map<String, Bitmap> mBitmap, ButtonFirst btn) {
        int TYPE_3 = getRandomNo();

        Intent intent;

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (mNotificationManager != null) {

            intent = new Intent(DataHubConstant.CUSTOM_ACTION);
            intent.addCategory(this.context.getPackageName());
            intent.putExtra(MapperUtils.keyType, btn.click_type);
            intent.putExtra(MapperUtils.keyValue, btn.click_value);

            PendingIntent pcloseIntent = PendingIntent.getActivity(context, TYPE_3, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_type3_one);
            contentView.setTextViewText(R.id.title, this.push.headertext);
            contentView.setTextColor(R.id.title, Color.parseColor(this.push.headertextcolor));

            if (mBitmap.get(push.icon_src) != null) {
                contentView.setImageViewBitmap(R.id.icon, mBitmap.get(this.push.icon_src));
            } else {
                contentView.setImageViewResource(R.id.icon, R.drawable.app_icon);
            }


            RemoteViews contentViewbig = new RemoteViews(context.getPackageName(), R.layout.notification_type3_onebig);
            contentViewbig.setTextViewText(R.id.title, this.push.headertext);
            contentViewbig.setTextColor(R.id.title, Color.parseColor(this.push.headertextcolor));
            contentViewbig.setTextViewText(R.id.button, btn.buttontext);
            contentViewbig.setTextColor(R.id.button, Color.parseColor(btn.buttontextcolor));
            contentViewbig.setImageViewBitmap(R.id.image, mBitmap.get(this.push.banner_src));

            if (mBitmap.get(push.icon_src) != null) {
                contentViewbig.setImageViewBitmap(R.id.icon, mBitmap.get(this.push.icon_src));
            } else {
                contentViewbig.setImageViewResource(R.id.icon, R.drawable.app_icon);
            }

            Notification notification;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(this.context.getResources().getString(R.string.fcm_defaultSenderId),
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(NOTIFICATION_CHANNEL_DISCRIPTION);

                mNotificationManager.createNotificationChannel(channel);

                Notification.Builder builder = new Notification.Builder(this.context,
                        this.context.getResources().getString(R.string.fcm_defaultSenderId))
                        .setContentTitle(push.headertext)
                        .setCustomContentView(contentView)
                        .setCustomBigContentView(contentViewbig);

                builder.setContentIntent(pcloseIntent);
                builder.setSmallIcon(R.drawable.status_app_icon);
                notification = builder.build();

            } else {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this.context,
                                this.context.getResources().getString(R.string.fcm_defaultSenderId))
                                .setCustomContentView(contentView)
                                .setCustomBigContentView(contentViewbig);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBuilder.setSmallIcon(R.drawable.status_app_icon);

                } else {
                    mBuilder.setSmallIcon(R.drawable.app_icon);
                }


                notification = mBuilder.build();
            }

            notification.contentIntent = pcloseIntent;

            if (this.push.cancelable.equalsIgnoreCase("yes")) {
                notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_AUTO_CANCEL;
            } else {
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
            }

            if (this.push.sound.equalsIgnoreCase("yes")) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            }
            if (this.push.vibration.equalsIgnoreCase("yes")) {
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            }

            mNotificationManager.notify(TYPE_3, notification);
        }
    }


    private void type4Notification(Map<String, Bitmap> mBitmap, ButtonFirst btn1, ButtonSecond btn2) {
        int TYPE_4 = getRandomNo();

        Intent intent1, intent2;

        // Add as notification
        NotificationManager mNotificationManager = (NotificationManager) this.context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null) {

            intent1 = new Intent(DataHubConstant.CUSTOM_ACTION);
            intent1.addCategory(this.context.getPackageName());
            intent1.putExtra(MapperUtils.keyType, btn1.click_type);
            intent1.putExtra(MapperUtils.keyValue, btn1.click_value);

            intent2 = new Intent(this.context, NotificationActionReceiver.class);
            intent2.setAction("sec_btn");
            intent2.putExtra("sec_btn_type", btn2.click_type);
            intent2.putExtra("sec_btn_value", btn2.click_value);
            intent2.putExtra("TYPE_4", TYPE_4);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            PendingIntent pendingSwitchIntent = PendingIntent.getActivity(this.context, TYPE_4, intent1,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent contentIntent2 = PendingIntent.getBroadcast(this.context, TYPE_4, intent2,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews contentView = new RemoteViews(this.context.getPackageName(), R.layout.notification_type3_two);
            contentView.setTextViewText(R.id.title, this.push.headertext);
            contentView.setTextColor(R.id.title, Color.parseColor(this.push.headertextcolor));

            if (mBitmap.get(this.push.icon_src) != null) {
                contentView.setImageViewBitmap(R.id.icon, mBitmap.get(this.push.icon_src));
            } else {
                contentView.setImageViewResource(R.id.icon, R.drawable.app_icon);
            }

            contentView.setOnClickPendingIntent(R.id.button2,
                    contentIntent2);

            RemoteViews contentViewbig = new RemoteViews(this.context.getPackageName(), R.layout.notification_type3_twobig);
            contentViewbig.setTextViewText(R.id.title, this.push.headertext);
            contentViewbig.setTextColor(R.id.title, Color.parseColor(this.push.headertextcolor));
            contentViewbig.setTextViewText(R.id.button, btn1.buttontext);
            contentViewbig.setTextColor(R.id.button, Color.parseColor(btn1.buttontextcolor));
            contentViewbig.setTextViewText(R.id.button2, btn2.buttontext);
            contentViewbig.setTextColor(R.id.button2, Color.parseColor(btn2.buttontextcolor));
            contentViewbig.setImageViewBitmap(R.id.image, mBitmap.get(this.push.banner_src));

            if (mBitmap.get(this.push.icon_src) != null) {
                contentViewbig.setImageViewBitmap(R.id.icon, mBitmap.get(this.push.icon_src));
            } else {
                contentViewbig.setImageViewResource(R.id.icon, R.drawable.app_icon);
            }

            contentViewbig.setOnClickPendingIntent(R.id.button2,
                    contentIntent2);

            Notification notification;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(this.context.getResources().getString(R.string.fcm_defaultSenderId),
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(NOTIFICATION_CHANNEL_DISCRIPTION);

                mNotificationManager.createNotificationChannel(channel);

                Notification.Builder builder = new Notification.Builder(this.context,
                        this.context.getResources().getString(R.string.fcm_defaultSenderId))
                        .setContentTitle(push.headertext)
                        .setCustomContentView(contentView)
                        .setCustomBigContentView(contentViewbig);

                builder.setContentIntent(pendingSwitchIntent);
                builder.setSmallIcon(R.drawable.status_app_icon);
                notification = builder.build();
            } else {
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this.context,
                                this.context.getResources().getString(R.string.fcm_defaultSenderId))
                                .setContentTitle(this.push.headertext)
                                .setCustomContentView(contentView)
                                .setCustomBigContentView(contentViewbig);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setSmallIcon(R.drawable.status_app_icon);

                } else {
                    builder.setSmallIcon(R.drawable.app_icon);
                }


                notification = builder.build();
            }
            notification.contentIntent = pendingSwitchIntent;


            if (this.push.cancelable.equalsIgnoreCase("yes")) {
                notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_AUTO_CANCEL;
            } else {
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
            }

            if (this.push.sound.equalsIgnoreCase("yes")) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            }
            if (this.push.vibration.equalsIgnoreCase("yes")) {
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            }

            mNotificationManager.notify(TYPE_4, notification);
        }
    }


    @Override
    public void onImageDownload(Map<String, Bitmap> mMap) {
        createNotification(mMap, this.push);
    }

    private ArrayList<String> getAllUrlstobeDOwnloadedFromPush(NotificationUIResponse push) {
        ArrayList<String> arrayList = new ArrayList<>();

        if (push.type.equalsIgnoreCase("type3")) {
            arrayList.add(push.icon_src);
            arrayList.add(push.banner_src);
        }
        return arrayList;
    }

    private int getRandomNo() {
        Random r = new Random();
        int low = 10;
        int high = 100;
        return r.nextInt(high - low) + low;
    }
}

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

import app.fcm.MapperUtils;
import app.fcm.NotificationUIResponse;
import app.fcm.imageparser.ImageDownloader;
import app.fcm.imageparser.LoadImage;
import app.pnd.adshandler.R;
import app.server.v2.DataHubConstant;

/**
 * Created by quantum4u1 on 27/04/18.
 */

public class Type2PushListener implements FCMType, ImageDownloader {

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int TYPE_2 = getRandomNo();

            Intent notificationIntent;

            NotificationManager mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            if (mNotificationManager != null) {

                notificationIntent = new Intent(DataHubConstant.CUSTOM_ACTION);
                notificationIntent.addCategory(this.context.getPackageName());
                notificationIntent.putExtra(MapperUtils.keyType, push.click_type);
                notificationIntent.putExtra(MapperUtils.keyValue, push.click_value);

                PendingIntent contentIntent = PendingIntent.getActivity(context, TYPE_2, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_type2);
                contentView.setTextViewText(R.id.title, push.headertext);
                contentView.setTextColor(R.id.title, Color.parseColor(push.headertextcolor));
                contentView.setTextViewText(R.id.contentTitle, push.footertext);
                contentView.setTextColor(R.id.contentTitle, Color.parseColor(push.footertextcolor));

                if (mBitmap.get(push.icon_src) != null) {
                    contentView.setImageViewBitmap(R.id.icon, mBitmap.get(push.icon_src));
                } else {
                    contentView.setImageViewResource(R.id.icon, R.drawable.app_icon);
                }


                RemoteViews contentViewBig = new RemoteViews(context.getPackageName(), R.layout.notification_type2_big);
                contentViewBig.setTextViewText(R.id.title, push.headertext);
                contentViewBig.setTextColor(R.id.title, Color.parseColor(push.headertextcolor));
                contentViewBig.setTextViewText(R.id.contentTitle, push.footertext);
                contentViewBig.setTextColor(R.id.contentTitle, Color.parseColor(push.footertextcolor));


                if (mBitmap.get(push.icon_src) != null) {
                    contentViewBig.setImageViewBitmap(R.id.icon, mBitmap.get(push.icon_src));
                } else {
                    contentViewBig.setImageViewResource(R.id.icon, R.drawable.app_icon);
                }
                contentViewBig.setImageViewBitmap(R.id.image, mBitmap.get(push.banner_src));

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
                            .setCustomBigContentView(contentViewBig);

                    builder.setContentIntent(contentIntent);
                    builder.setSmallIcon(R.drawable.ic_stat_action_settings_phone);
                    notification = builder.build();

                } else {
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this.context,
                                    this.context.getResources().getString(R.string.fcm_defaultSenderId))
                                    .setContentTitle(push.headertext)
                                    .setCustomContentView(contentView)
                                    .setCustomBigContentView(contentViewBig);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBuilder.setSmallIcon(R.drawable.ic_stat_action_settings_phone);

                    } else {
                        mBuilder.setSmallIcon(R.drawable.app_icon);
                    }


                    notification = mBuilder.build();
                }

                notification.contentIntent = contentIntent;

                if (push.cancelable.equalsIgnoreCase("yes")) {
                    notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_AUTO_CANCEL;
                } else {
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                }

                if (push.sound.equalsIgnoreCase("yes")) {
                    notification.defaults |= Notification.DEFAULT_SOUND;
                }
                if (push.vibration.equalsIgnoreCase("yes")) {
                    notification.defaults |= Notification.DEFAULT_VIBRATE;
                }

                mNotificationManager.notify(TYPE_2, notification);

            }
        } else {
            new Type1PushListener().generatePush(this.context, this.push);
        }

    }

    @Override
    public void onImageDownload(Map<String, Bitmap> mMap) {
        createNotification(mMap, this.push);
    }

    private ArrayList<String> getAllUrlstobeDOwnloadedFromPush(NotificationUIResponse push) {
        ArrayList<String> arrayList = new ArrayList<>();

        if (push.type.equalsIgnoreCase("type2")) {
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

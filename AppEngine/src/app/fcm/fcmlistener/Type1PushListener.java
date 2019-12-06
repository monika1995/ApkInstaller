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

import java.util.Map;
import java.util.Random;

import app.PrintLog;
import app.fcm.MapperUtils;
import app.fcm.NotificationUIResponse;
import app.fcm.imageparser.ImageDownloader;
import app.fcm.imageparser.LoadImage;
import app.pnd.adshandler.R;
import app.server.v2.DataHubConstant;
import app.serviceprovider.Utils;

/**
 * Created by quantum4u1 on 27/04/18.
 */

public class Type1PushListener implements FCMType, ImageDownloader {

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

            if (r.icon_src != null
                    && !r.icon_src.equalsIgnoreCase("NA")
                    && !r.icon_src.equalsIgnoreCase("")) {

                new LoadImage(c, r.icon_src, this).startDownload();
            } else {
                createNotification(Utils.getBitmapFromDrawable(this.context.getResources().getDrawable(R.drawable.app_icon)), this.push);
            }
        }
    }

    private void createNotification(Bitmap bitmap, NotificationUIResponse push) {
        int TYPE_1 = getRandomNo();
        PrintLog.print("Before lunch logs 03");

        Intent intent;
        // Add as notification
        NotificationManager manager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {

            intent = new Intent(DataHubConstant.CUSTOM_ACTION);
            intent.addCategory(this.context.getPackageName());
            intent.putExtra(MapperUtils.keyType, push.click_type);
            intent.putExtra(MapperUtils.keyValue, push.click_value);

            PendingIntent contentIntent = PendingIntent.getActivity(this.context, TYPE_1, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews contentView = new RemoteViews(this.context.getPackageName(), R.layout.notification_type1);
            contentView.setTextViewText(R.id.title, push.headertext);
            contentView.setTextColor(R.id.title, Color.parseColor(push.headertextcolor));
            contentView.setTextViewText(R.id.contentTitle, push.footertext);
            contentView.setTextColor(R.id.contentTitle, Color.parseColor(push.footertextcolor));
            contentView.setTextViewText(R.id.button, push.buttontext);
            contentView.setTextColor(R.id.button, Color.parseColor(push.buttontextcolor));
            contentView.setImageViewBitmap(R.id.imageView, bitmap);

            RemoteViews contentViewBig = new RemoteViews(context.getPackageName(), R.layout.notification_type1_big);
            contentViewBig.setTextViewText(R.id.title, push.headertext);
            contentViewBig.setTextColor(R.id.title, Color.parseColor(push.headertextcolor));
            contentViewBig.setTextViewText(R.id.contentTitle, push.footertext);
            contentViewBig.setTextColor(R.id.contentTitle, Color.parseColor(push.footertextcolor));
            contentViewBig.setTextViewText(R.id.button, push.buttontext);
            contentViewBig.setTextColor(R.id.button, Color.parseColor(push.buttontextcolor));
            contentViewBig.setImageViewBitmap(R.id.imageView, bitmap);
            contentViewBig.setTextViewText(R.id.expandbigtext, push.footertext);
            contentViewBig.setTextColor(R.id.expandbigtext, Color.parseColor(push.footertextcolor));

            Notification notification;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(this.context.getResources().getString(R.string.fcm_defaultSenderId),
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(NOTIFICATION_CHANNEL_DISCRIPTION);
                manager.createNotificationChannel(channel);

                Notification.Builder builder = new Notification.Builder(this.context,
                        this.context.getResources().getString(R.string.fcm_defaultSenderId))
                        .setContentTitle(push.headertext)
                        .setCustomContentView(contentView)
                        .setCustomBigContentView(contentViewBig);

                builder.setContentIntent(contentIntent);
                builder.setSmallIcon(R.drawable.status_app_icon);
                notification = builder.build();

            } else {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context,
                        this.context.getResources().getString(R.string.fcm_defaultSenderId))
                        .setContentTitle(push.headertext)
                        .setCustomContentView(contentView)
                        .setCustomBigContentView(contentViewBig);


                builder.setContentIntent(contentIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setSmallIcon(R.drawable.status_app_icon);
                } else {
                    builder.setSmallIcon(R.drawable.app_icon);
                }

                notification = builder.build();
            }
            //notification.contentView = contentView;
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

            manager.notify(TYPE_1, notification);
        }

    }

    @Override
    public void onImageDownload(Map<String, Bitmap> mMap) {
        createNotification(mMap.get(this.push.icon_src), this.push);
    }


    private int getRandomNo() {
        Random r = new Random();
        int low = 10;
        int high = 100;
        return r.nextInt(high - low) + low;
    }
}

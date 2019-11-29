package app.fcm.fcmlistener;

import android.content.Context;
import android.content.Intent;

import app.PrintLog;
import app.fcm.NotificationTypeFour;
import app.fcm.NotificationUIResponse;

/**
 * Created by quantum4u1 on 27/04/18.
 */

public class DesktopNotification implements FCMType {

    @Override
    public void generatePush(Context c, NotificationUIResponse r) {
        try {
            Intent intent = new Intent(c, NotificationTypeFour.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("imgsrc", r.banner_src);
            intent.putExtra("clicktype", r.click_type);
            intent.putExtra("clickvalue", r.click_value);
            c.startActivity(intent);

        } catch (Exception e) {
            PrintLog.print("getNotificationValue.onPostExecute Exception" + e);

        }
    }
}

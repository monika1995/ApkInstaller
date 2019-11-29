package app.fcm.fcmlistener;

import android.content.Context;

import app.fcm.NotificationUIResponse;

/**
 * Created by quantum4u1 on 27/04/18.
 */

public interface FCMType {

    void generatePush(Context c, NotificationUIResponse r);

}

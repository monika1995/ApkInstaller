package app.fcm;

import android.content.Context;

import app.fcm.fcmlistener.FCMFactory;

/**
 * Created by quantum4u1 on 27/04/18.
 */

public class FCMController {

    public FCMController(Context context, NotificationUIResponse response) {
        new FCMFactory().getPushType(response).generatePush(context, response);
    }
}

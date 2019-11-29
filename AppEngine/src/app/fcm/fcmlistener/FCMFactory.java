package app.fcm.fcmlistener;


import app.fcm.NotificationUIResponse;

/**
 * Created by quantum4u1 on 27/04/18.
 */

public class FCMFactory {


    public FCMType getPushType(NotificationUIResponse r) {
        if (r == null)
            return null;

        if (r.type.equalsIgnoreCase("type1"))
            return new Type1PushListener();

        if (r.type.equalsIgnoreCase("type2"))
            return new Type2PushListener();

        if (r.type.equalsIgnoreCase("type3"))
            return new Type3PushListener();

        if (r.type.equalsIgnoreCase("type4"))
            return new DesktopNotification();

        if (r.type.equalsIgnoreCase("type5"))
            return new Type5PushListener();

        return null;
    }
}

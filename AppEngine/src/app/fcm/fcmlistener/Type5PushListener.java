package app.fcm.fcmlistener;

import android.content.Context;
import android.content.Intent;

import app.PrintLog;
import app.fcm.MapperUtils;
import app.fcm.NotificationUIResponse;
import app.server.v2.DataHubConstant;

public class Type5PushListener implements FCMType {

    @Override
    public void generatePush(Context c, NotificationUIResponse r) {
        try {
            Intent intent = new Intent(DataHubConstant.CUSTOM_ACTION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(c.getPackageName());
            intent.putExtra(MapperUtils.keyType, r.type);
            intent.putExtra(MapperUtils.keyValue, r.click_value);
            c.startActivity(intent);

        } catch (Exception e) {
            PrintLog.print("getNotificationValue.onPostExecute Exception" + e);

        }
    }
}

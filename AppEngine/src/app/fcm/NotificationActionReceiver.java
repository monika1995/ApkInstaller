package app.fcm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.PrintLog;
import app.server.v2.DataHubConstant;

/**
 * Created by rajeev on 10/04/18.
 */

public class NotificationActionReceiver extends BroadcastReceiver {
    Intent intent2;
    int TYPE_4;

    @Override
    public void onReceive(Context context, Intent intent) {
        PrintLog.print("NotificationActionReceiver.onReceive ");
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equalsIgnoreCase("sec_btn")) {
                TYPE_4 = intent.getIntExtra("TYPE_4", 0);
                String clickType2 = intent.getStringExtra("sec_btn_type");
                String clickValue2 = intent.getStringExtra("sec_btn_value");

                PrintLog.print("NotificationActionReceiver.onReceive 01 " + clickType2 + " " + clickValue2);

                intent2 = new Intent(DataHubConstant.CUSTOM_ACTION);
                intent2.addCategory(context.getPackageName());
                intent2.putExtra(MapperUtils.keyType, clickType2);
                intent2.putExtra(MapperUtils.keyValue, clickValue2);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent2);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null)
                    notificationManager.cancel(TYPE_4);
            }
        }

    }
}

package app.fcm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.PrintLog;
import app.pnd.adshandler.R;
import app.server.v2.DataHubConstant;


public class NotificationTypeFour extends Activity {
    ImageView adsimage;
    Button exit;
    public static String src = "";
    public static String clicktype = "";
    public static String clickvalue = "";
    Intent intent;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.notification_type4);
        adsimage = findViewById(R.id.adsimage);
        exit = findViewById(R.id.exit);

        PrintLog.print("GCM CP SRC " + src);
        PrintLog.print("GCM CP clicktype " + clicktype);
        PrintLog.print("GCM CP clickvalue " + clickvalue);

        if (getIntent().getExtras() != null) {
            src = getIntent().getExtras().getString("imgsrc");
            clicktype = getIntent().getExtras().getString("clicktype");
            clickvalue = getIntent().getExtras().getString("clickvalue");
        }

        if (src != null && !src.equalsIgnoreCase("")) {
            Picasso.get()
                    .load(src)
                    .into(adsimage);
        }
        exit.setOnClickListener(new View.OnClickListener() {


            public void onClick(View arg0) {
                finish();

            }
        });


        adsimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (clicktype != null && clickvalue != null) {
                    intent = new Intent(DataHubConstant.CUSTOM_ACTION);
                    intent.addCategory(getPackageName());
                    intent.putExtra(MapperUtils.keyType, clicktype);
                    intent.putExtra(MapperUtils.keyValue, clickvalue);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(DataHubConstant.CUSTOM_ACTION);
                    intent.addCategory(getPackageName());
                    startActivity(intent);
                    finish();
                }


            }
        });


    }

}

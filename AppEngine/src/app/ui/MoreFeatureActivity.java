package app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import app.pnd.adshandler.R;

/**
 * Created by qunatum4u2 on 04/01/19.
 */

public class MoreFeatureActivity extends Activity {

    private String clickurl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_feature_layout);
        CardView cardView_dp = findViewById(R.id.cardView_dp);
        ImageView image1 = findViewById(R.id.image1);


        Intent intent = getIntent();

        clickurl = intent.getStringExtra("clickurl");
        String bannerurl = intent.getStringExtra("bannerurl");


        //Log.d("MoreFeatureActivity", "Hello onCreate tttt" + " " + bannerurl);

        if (bannerurl != null) {
            Picasso.get().load(bannerurl).into(image1);

        }


        cardView_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickurl != null) {
                    openUrl(MoreFeatureActivity.this, clickurl);
                }
            }
        });


    }


    public void openUrl(Context context, String clickurl) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(clickurl));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



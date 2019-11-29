package app.campaign;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import app.pnd.adshandler.R;

/**
 * Created by hp on 7/20/2017.
 */
public class CampaignConstant {

    public static final String FORMAT_IMAGE = "image";
    static final String FORMAT_TEXT = "text";

    public static final String CAMPAIGN_TYPE_URL = "URL";
    public static final String CAMPAIGN_TYPE_DEEPLINK = "DEEPLINK";
    public static final String CAMPAIGN_TYPE_ADS = "ADS";

    public static final String ADS_BANNER = "BANNER";
    public static final String ADS_NATIVE_LARGE = "N_L";
    public static final String ADS_NATIVE_MEDIUM = "N_M";
    public static final String ADS_NATIVE_SMALL = "N_S";

    public static final String DEEPLINK_KEY = "value_key";


    private Context mContext;

    public CampaignConstant(Context c) {
        this.mContext = c;
    }


    public void openURL(Context c, String url) {
        PackageManager manager = c.getPackageManager();
        if (url != null && !url.equalsIgnoreCase("") && url.contains("http://")) {
            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
            if (intent.resolveActivity(manager) != null)
                c.startActivity(intent);
            else
                Toast.makeText(mContext, "Oops! No application found to handle this", Toast.LENGTH_SHORT).show();
        }
    }

    public void openDeepLink(Context c, String deeplink) {
        // Toast.makeText(mContext, "DeepLinke Id" + " " + deeplink, Toast.LENGTH_SHORT).show();
//        c.startActivity(new Intent(c, MapperActivity.class).
//                putExtra(CampaignConstant.DEEPLINK_KEY, deeplink));
    }

    public String readFromAssets(String filename) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // do reading, usually loop until end of file reading
        StringBuilder sb = new StringBuilder();
        String mLine = null;
        try {
            mLine = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (mLine != null) {
            sb.append(mLine); // process line
            try {
                mLine = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("check for logs 01");
        return sb.toString();
    }

    public void crossPromotionDialog(Context context, OnCPDialogClick onCPDialogClick, String header, String mDescription, String bgColor, String textColor, String cpURL) {
        mCPDialogClick = onCPDialogClick;
        final Dialog dialog = new Dialog(context, R.style.BaseTheme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.remove_ads);

        Picasso.get().load(cpURL).placeholder(R.drawable.app_icon).into((ImageView) dialog.findViewById(R.id.cpIcon));

        TextView btn_dismiss = dialog.findViewById(R.id.btn_dismiss);
        TextView btn_download_now = dialog.findViewById(R.id.btn_download_now);

        TextView title = dialog.findViewById(R.id.title);
        TextView description = dialog.findViewById(R.id.description);
        FrameLayout frame_dialog = dialog.findViewById(R.id.frame_dialog);

        GradientDrawable gd = (GradientDrawable) frame_dialog.getBackground();
        gd.setColor(Color.parseColor(bgColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            frame_dialog.setBackground(gd);
        }

        title.setText(textColor);
        description.setText(textColor);

        title.setText(header);
        description.setText(mDescription);

        btn_download_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mCPDialogClick.clickOK();
            }
        });

        btn_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    private OnCPDialogClick mCPDialogClick;

    public interface OnCPDialogClick {
        void clickOK();
    }
}

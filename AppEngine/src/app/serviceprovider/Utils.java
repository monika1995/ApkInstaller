package app.serviceprovider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import app.PrintLog;
import app.pnd.adshandler.R;
import app.server.v2.Slave;

/**
 * Created by Meenu Singh on 12/06/19.
 */

public class Utils {
    public static final String SHOW_VALUE = "show_value";
    public static final int SHOW_ASSET_VALUE = 1;
    public static final int SHOW_SERVER_VALUE = 2;

    public static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            assert cm != null;
            return cm.getActiveNetworkInfo() != null;
        } catch (Exception e) {
            return true;
        }
    }


    public static int getRandomNo(int delayTime) {
        Random r = new Random();
        int low = 60 * 1000;
        int high = delayTime * 60 * 60 * 1000;
        return r.nextInt(high - low) + low;
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public void sendFeedback(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Slave.FEEDBACK_email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_TEXT, ""
                + context.getResources().getString(R.string.app_name) + "\n"
                + "Device Brand:   " + Build.BRAND + "\nDevice Model: "
                + Build.MODEL + "\nDevice Version: "
                + Build.VERSION.SDK_INT);
        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        context.startActivity(emailIntent);

    }

    private String mailBody = "";

    public void shareUrl(Context context) {
        String link_val;
        link_val = Slave.SHARE_URL;
        mailBody += "Hi, Download this cool and fast performance App\n";
        mailBody = Slave.SHARE_TEXT + " ";
        mailBody += link_val;
        mailBody += "";

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);

        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(Html.fromHtml(mailBody))); // mailBody
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(Intent
                .createChooser(sharingIntent, "Share using"));
    }

    public void rateUs(Context context) {
        try {
            PrintLog.print("Rate App URL is " + Slave.RATE_APP_rateurl);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Slave.RATE_APP_rateurl));

            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        } catch (Exception e) {
            PrintLog.print("Rate App URL is exp  " + e.getMessage());
        }
    }

    public void moreApps(Context context) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Slave.MOREAPP_moreurl));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static int get_FulladsshownCount(Context context) {

        SharedPreferences FulladsshownCount = context.getSharedPreferences(
                "FulladsshownCount", Activity.MODE_PRIVATE);
        return FulladsshownCount.getInt("FulladsshownCount", 0);

    }

    static void set_FulladsshownCount(Context context, int data) {
        SharedPreferences FulladsshownCount = context.getSharedPreferences(
                "FulladsshownCount", Activity.MODE_PRIVATE);
        Editor editor = FulladsshownCount.edit();
        editor.putInt("FulladsshownCount", data);
        editor.apply();
    }

    private static int get_fullservicecount(Context context) {
        SharedPreferences fullservicecount = context.getSharedPreferences(
                "fullservicecount", Activity.MODE_PRIVATE);
        return fullservicecount.getInt("fullservicecount", 0);

    }

    private static void set_fullservicecount(Context context, int data) {
        SharedPreferences fullservicecount = context.getSharedPreferences(
                "fullservicecount", Activity.MODE_PRIVATE);
        Editor editor = fullservicecount.edit();
        editor.putInt("fullservicecount", data);
        editor.apply();
    }


    private static int get_fullservicecount_start(Context context) {
        SharedPreferences fullservicecount = context.getSharedPreferences(
                "fullservicecount_start", Activity.MODE_PRIVATE);
        return fullservicecount.getInt("fullservicecount_start", 0);

    }

    private static void set_fullservicecount_start(Context context, int data) {
        SharedPreferences fullservicecount = context.getSharedPreferences(
                "fullservicecount_start", Activity.MODE_PRIVATE);
        Editor editor = fullservicecount.edit();
        editor.putInt("fullservicecount_start", data);
        editor.apply();
    }


    private static int get_fullservicecount_exit(Context context) {
        SharedPreferences fullservicecount = context.getSharedPreferences(
                "fullservicecount_exit", Activity.MODE_PRIVATE);
        return fullservicecount.getInt("fullservicecount_exit", 0);

    }

    private static void set_fullservicecount_exit(Context context, int data) {
        SharedPreferences fullservicecount = context.getSharedPreferences(
                "fullservicecount_exit", Activity.MODE_PRIVATE);
        Editor editor = fullservicecount.edit();
        editor.putInt("fullservicecount_exit", data);
        editor.apply();
    }

    public void showPrivacyPolicy(final Context mContext, View mLayout, boolean isFirstTime) {
        LinearLayout linearLayout = mLayout.findViewById(R.id.bottom);
        TextView tvTerms = mLayout.findViewById(R.id.tvTerms);
        TextView tvPrivacy = mLayout.findViewById(R.id.tvPrivacy);

        tvTerms.setText(Html.fromHtml("<u>Terms of Service</u>"));
        tvPrivacy.setText(Html.fromHtml("<u>Privacy Policy</u>"));

        if (!isFirstTime) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }

        tvTerms.setClickable(true);
        tvTerms.setMovementMethod(LinkMovementMethod.getInstance());
        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_TERM_AND_COND)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tvPrivacy.setClickable(true);
        tvPrivacy.setMovementMethod(LinkMovementMethod.getInstance());

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Slave.ABOUTDETAIL_PRIVACYPOLICY)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void showSharePrompt(final Context context, String msg) {
        AlertDialog.Builder dia = new AlertDialog.Builder(context).setTitle(context.getApplicationContext()
                .getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton("Share",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                shareUrl(context);

                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dia.setCancelable(true);
        //dia.show();

        AlertDialog alert = dia.create();

        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);

        alert.show();
    }

    public void showFeedbackPrompt(final Context context, String msg) {
        AlertDialog.Builder dia = new AlertDialog.Builder(context).setTitle("Thanks for using " + context.getApplicationContext()
                .getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton("Send",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sendFeedback(context);
                            }
                        })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dia.setCancelable(true);
        //dia.show();

        AlertDialog alert = dia.create();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);

        alert.show();

    }

    public void showAppUpdatePrompt(final Activity context) {
        AlertDialog.Builder dia =
                new AlertDialog.Builder(context)
                        .setTitle("New Update is Available")
                        .setMessage(Slave.UPDATES_prompttext)
                        .setPositiveButton("UPDATE NOW", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //aHandler.showFullAds(MainActivity.this,false);
                                new Utils().openPlayStore(Slave.UPDATES_appurl, context);
                            }
                        })
                        .setNegativeButton("LATER", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.app_icon).setCancelable(false);
        dia.setCancelable(true);
        //dia.show();

        AlertDialog alert = dia.create();
        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLACK);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);

        alert.show();

    }

    private void openPlayStore(String url, Context c) {

        try {
            c.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url)));
        } catch (android.content.ActivityNotFoundException anfe) {
            final String appPackageName = c.getPackageName();
            c.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private static long getInstalltionTime(Context context) {

        long installed = System.currentTimeMillis();
        try {
            installed = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0).firstInstallTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return installed;
    }

    public static long getDaysDiff(Context context) {
        try {
            PrintLog.print("getInstalltionTime(context) 0 stage ");
            PrintLog.print("getInstalltionTime(context) " + getInstalltionTime(context));
            long day = System.currentTimeMillis() - getInstalltionTime(context);
            day = day / (1000 * 60 * 60 * 24);
            return day;


        } catch (Exception e) {
            return 0;
        }
    }

  /*  public static void sortProviderList(final List<AdsProviders> list) {
        Collections.sort(list, new Comparator<AdsProviders>() {
            @Override
            public int compare(AdsProviders o1, AdsProviders o2) {
                return o1.priority.compareToIgnoreCase(o2.priority);
                //return o2.priority.compareToIgnoreCase(o1.priority);
            }
        });
    }*/

    public static int getStringtoInt(String data) {
        try {
            return Integer.parseInt(data);
        } catch (Exception e) {
            return 0;
        }

    }

    public static String getAppName(Context context) {
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }

        return (String) (ai != null ? pm.getApplicationLabel(ai) : "Dear");
    }

    public static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * set and get full ads count
     */
    public static void setFulladsCount(Activity activity, int data) {
        int value = Utils.get_fullservicecount(activity) + 1;
        if (data >= 0) {
            value = data;
        }
        PrintLog.print("Full Nav Adder setter " + value);
        Utils.set_fullservicecount(activity, value);
    }

    public static int getFullAdsCount(Activity cActivity) {
        int data = Utils.get_fullservicecount(cActivity);
        PrintLog.print("Full Nav Adder getter " + data);
        return data;
    }

    /**
     * set and get start full ads count
     */
    public static void setFulladsCount_start(Activity activity, int data) {
        int value = Utils.get_fullservicecount_start(activity) + 1;
        if (data >= 0) {
            value = data;
        }
        PrintLog.print("Full Nav Start Adder setter " + value);
        Utils.set_fullservicecount_start(activity, value);
    }

    public static int getFullAdsCount_start(Activity cActivity) {
        int data = Utils.get_fullservicecount_start(cActivity);
        PrintLog.print("Full Nav Start getter " + data);
        return data;
    }

    /**
     * set and get exit full ads count
     */
    public static void setFulladsCount_exit(Activity activity, int data) {
        int value = Utils.get_fullservicecount_exit(activity) + 1;
        if (data >= 0) {
            value = data;
        }
        PrintLog.print("Full Nav Exit Adder setter " + value);
        Utils.set_fullservicecount_exit(activity, value);
    }

    public static int getFullAdsCount_exit(Activity cActivity) {
        int data = Utils.get_fullservicecount_exit(cActivity);
        PrintLog.print("Full Nav Exit getter " + data);
        return data;
    }

    @NonNull
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * remember to change color according to your app theme.
     */
    public static void showFAQs(Activity mContext) {
        try {
            if (Slave.ABOUTDETAIL_FAQ != null && !Slave.ABOUTDETAIL_FAQ.equalsIgnoreCase("")) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                builder.addDefaultShareMenuItem();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(mContext, Uri.parse(Slave.ABOUTDETAIL_FAQ));
            } else {
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

 /*   public static void copyToClipBoard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
    }*/

}

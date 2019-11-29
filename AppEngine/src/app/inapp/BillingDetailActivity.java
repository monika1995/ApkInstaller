package app.inapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.PrintLog;
import app.pnd.adshandler.R;
import app.server.v2.Billing;
import app.server.v2.BillingResponseHandler;
import app.server.v2.DataHubPreference;
import app.server.v2.Slave;
import util.IabHelper;
import util.IabResult;
import util.Inventory;
import util.Purchase;

/**
 * Created by quantum4u1 on 17/04/18.
 */

public class BillingDetailActivity extends Activity {

    //    private InAppHandler mHandler;

    private IabHelper mHelper;

    private BillingPreference mPreference;
    private static final int RC_REQUEST = 74;

    private String productName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_details_layout);


        mPreference = new BillingPreference(this);

        LinearLayout layoutPurchase = findViewById(R.id.layoutPurchase);
        TextView header = findViewById(R.id.header);

        Typeface headerTypeFace = Typeface.createFromAsset(getAssets(), "fonts/billing_regular.ttf");
        header.setTypeface(headerTypeFace);


        ImageView detail_src = findViewById(R.id.details_src);
        LinearLayout details_description = findViewById(R.id.details_description);

        TextView btn_text = findViewById(R.id.btn_text);
        TextView btn_subtext = findViewById(R.id.btn_subtext);

//        mHandler = new InAppHandler(this, Slave.INAPP_PUBLIC_KEY);
        mHelper = new IabHelper(this, Slave.INAPP_PUBLIC_KEY);
        initializeBilling();

//        mHandler = new BillingHandler(BillingDetailActivity.this, new InAppHandler.onInitializeListener() {
//            @Override
//            public void onInitializationDone(String message) {
//                PrintLog.print("<<<<here is the initDone 01" + " " + message);
//            }
//
//            @Override
//            public void onPurchaseFinishedListener(boolean isPremiun) {
//                PrintLog.print("<<<<here is the initDone 02");
//            }
//        });

        Intent mIntent = getIntent();
        final Billing b = (Billing) mIntent.getSerializableExtra("billing");
        productName = b.product_offer_text;

        PrintLog.print("1109 checking all 02" + " " + b.details_page_type);
        if (b.details_page_type.equalsIgnoreCase("description")) {
            PrintLog.print("1109 checking all 03" + " ");
            details_description.setVisibility(View.VISIBLE);
            detail_src.setVisibility(View.GONE);

//            details_description.setText(b.details_description);

            String[] descrption = b.details_description.split("\n");
            for (String s : descrption) {
                Typeface descriptionTypeFace = Typeface.createFromAsset(getAssets(), "fonts/billing_light.ttf");
                TextView textView = new TextView(BillingDetailActivity.this);
                textView.setTextColor(getResources().getColor(android.R.color.white));
                textView.setTextSize(17);
                textView.setTypeface(descriptionTypeFace);

                if (!s.equalsIgnoreCase("")) {
                    textView.setCompoundDrawablePadding(20);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.billing_check, 0, 0, 0);
                }
                textView.setText(s);
                details_description.addView(textView);
            }


        } else if (b.details_page_type.equalsIgnoreCase("image")) {
            PrintLog.print("1109 checking all 04" + " ");
            details_description.setVisibility(View.GONE);
            detail_src.setVisibility(View.VISIBLE);

            if (b.details_src != null && !b.details_src.equalsIgnoreCase("")) {
                Picasso.get().load(b.details_src).into(detail_src);
            }

        }
//            }
        PrintLog.print("1109 checking all 05" + " " + b.button_text + " :sub::" + " " + b.button_sub_text);
        btn_text.setText(Html.fromHtml(b.button_text));
        btn_subtext.setText(Html.fromHtml(b.button_sub_text));
        header.setText(b.product_offer_text);

        if (b.billing_type.equalsIgnoreCase("free")) {
            PrintLog.print("1100 checking inside" + " " + b.billing_type);
            layoutPurchase.setVisibility(View.INVISIBLE);
        }

        layoutPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    /*new BillingHandler(BillingDetailActivity.this, null).initPremium(new InAppHandler.onInitializeListener() {
                        @Override
                        public void onInitializationDone() {
                            PrintLog.print("BILLING DETAILS INAPP 01");
                        }

                        @Override
                        public void onPurchaseFinishedListener(boolean isPremiun) {
                            PrintLog.print("BILLING DETAILS INAPP 001" + " " + isPremiun);
                        }
                    }, b.product_id);*/

                if (b.billing_type.equalsIgnoreCase("pro")) {

                   /* mHandler.initPremium(new InAppHandler.onInitializeListener() {
                        @Override
                        public void onInitializationDone(String message) {
                            PrintLog.print("<<<<here is the init done 011" + " " + message);
                        }

                        @Override
                        public void onPurchaseFinishedListener(boolean isPremiun) {
                            PrintLog.print("<<<<here is the finish 011" + " " + isPremiun);
                        }
                    }, b.product_id, false);*/
//                    mHandler.initiatePurchase(b.product_id, false);


                    String payload = "";

                    try {
                        mHelper.launchPurchaseFlow(BillingDetailActivity.this, b.product_id, RC_REQUEST,
                                mPurchaseFinishedListener, payload);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                } else {
                   /* mHandler.initPremium(new InAppHandler.onInitializeListener() {
                        @Override
                        public void onInitializationDone(String message) {
                            PrintLog.print("<<<<here is the initDone 03" + " " + message);
                        }

                        @Override
                        public void onPurchaseFinishedListener(boolean isPremiun) {
                            PrintLog.print("<<<<here is the finish 011" + " " + isPremiun);
                        }
                    }, b.product_id, true);*/
//                    mHandler.initiatePurchase( b.product_id, true);


                    if (!mHelper.subscriptionsSupported()) {
                        System.out.println("Subscriptions not supported on your device yet. Sorry!");
                        return;
                    }


                    String payload = "";

                    System.out.println("Launching purchase flow for monthly subscription.");
                    try {
                        mHelper.launchPurchaseFlow(BillingDetailActivity.this,
                                b.product_id, IabHelper.ITEM_TYPE_SUBS, null,
                                RC_REQUEST, mPurchaseFinishedListener, payload);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

//        }

        findViewById(R.id.iv_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.tv_other_plans).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PrintLog.print("onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;
        System.out.println("onActivityResult 01");
        // Pass on the activity result to the helper for handling


        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            System.out.println("onActivityResult 02");
            super.onActivityResult(requestCode, resultCode, data);
            System.out.println("onActivityResult 03");

        } else {
            System.out.println("onActivityResult 04");
            PrintLog.print("onActivityResult handled by IABUtil.");
        }


    }


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            System.out.println("inside iab purchase finish 01");

            if (mHelper == null) return;

            if (result.isFailure()) {
                System.out.println("Error purchasing: " + result);
                return;
            }

            System.out.println("purchase successful");

            for (Billing b : BillingResponseHandler.getInstance().getBillingResponse()) {
                if (purchase.getSku().equals(b.product_id)) {
                    switch (b.billing_type) {
                        case "pro":
                            System.out.println("Thank you for upgrading to premium!");
                            mIsPremium = true;

                            mPreference.setPro(mIsPremium);
                            Slave.IS_PRO = mPreference.isPro();

//                            mOnInitListener.onPurchaseFinishedListener(mIsPremium);

                            System.out.println("INAPP CHECKS HANDLER 02 " + mIsPremium + " " + Slave.IS_PRO);
                            showPurchaseDialog();
                            break;

                        case "weekly":
                            System.out.println("Thank you for upgrading to weekly premium membership");
                            mSubscribedToWeekly = true;

                            mPreference.setWeekly(mSubscribedToWeekly);
                            Slave.IS_WEEKLY = mPreference.isWeekly();


                            System.out.println("INAPP CHECKS HANDLER 04 " + mSubscribedToWeekly + " " + Slave.IS_WEEKLY);
//                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToWeekly);
                            showPurchaseDialog();
                            break;

                        case "monthly":
                            System.out.println("Thank you for upgrading to monthly premium membership");
                            mSubscribedToMonthly = true;

                            mPreference.setMonthly(mSubscribedToMonthly);
                            Slave.IS_MONTHLY = mPreference.isMonthly();

                            System.out.println("INAPP CHECKS HANDLER 05 " + mSubscribedToMonthly + " " + Slave.IS_MONTHLY);
//                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToMonthly);
                            showPurchaseDialog();
                            break;

                        case "halfYear":
                            System.out.println("Thank you for upgrading to premium!");
                            mSubscribedToHalfYearly = true;

                            mPreference.setHalfYearly(mSubscribedToHalfYearly);
                            Slave.IS_HALFYEARLY = mPreference.isHalfYearly();

                            System.out.println("INAPP CHECKS HANDLER 06 " + mSubscribedToHalfYearly + " " + Slave.IS_HALFYEARLY);
//                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToHalfYearly);
                            showPurchaseDialog();
                            break;

                        case "yearly":
                            System.out.println("Thank you for upgrading to premium!");
                            mSubscribedToYearly = true;

                            mPreference.setYearly(mSubscribedToYearly);
                            Slave.IS_YEARLY = mPreference.isYearly();

                            System.out.println("INAPP CHECKS HANDLER 07 " + mSubscribedToYearly + " " + Slave.IS_YEARLY);
//                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToYearly);
                            showPurchaseDialog();
                            break;
                        default:
                            System.out.println("INAPP CHECKIS HANDLER dafault case");
                            break;
                    }

                }
            }
        }
    };

    private void showPurchaseDialog() {
        final Dialog dialog = new Dialog(BillingDetailActivity.this, R.style.BaseTheme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.purchase_ok);


        TextView header = dialog.findViewById(R.id.tv_header);
        TextView description = dialog.findViewById(R.id.tv_description);

        DataHubPreference dP = new DataHubPreference(BillingDetailActivity.this);
        String headerText = "<b>" + dP.getAppname() + "</b>";
        header.setText(Html.fromHtml(headerText + " User,"));

        String descriptionText = "You have been upgraded to " + "<b>" + productName + "</b>" + " successfully.";

        description.setText(Html.fromHtml(descriptionText));

        LinearLayout restartLater = dialog.findViewById(R.id.restartLater);
        LinearLayout restartNow = dialog.findViewById(R.id.restartNow);


        restartLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                finish();
            }
        });

        restartNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartApplication();
            }
        });

        dialog.show();
    }


    private void restartApplication() {
        System.out.println("inside restart application");
        PrintLog.print("<<<<here is the initDone 04 bhanu");
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        if (i != null) {
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            System.exit(0);
        }
    }

    public void initializeBilling(/*onInitializeListener l*/) {
//        this.mOnInitListener = l;
        PrintLog.print("Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                PrintLog.print("Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    PrintLog.print("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                PrintLog.print("Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    PrintLog.print("Exception onTABSetupFinised" + " " + e);
                    e.printStackTrace();
                }
            }
        });
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            PrintLog.print("Query inventory finished.");
            String message;
            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                message = "Failed to query inventory";
                PrintLog.print("Failed to query inventory: " + result);
                return;
            }

            PrintLog.print("Query inventory was successful.");
            message = "Query inventory was successful.";

//            if (mOnInitListener != null) {
//                mOnInitListener.onInitializationDone(message);
//            }


            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            /*
            if(premiumPurchase.getPurchaseState()==0
                    || premiumPurchase.getPurchaseState()==2){
                //show premium version
            }else{
                //show free version
            }
            */


            String sku_pro, sku_weekly, sku_monthly, sku_halfyaer, sku_yearly;
            ArrayList<Billing> mBillingList = BillingResponseHandler.getInstance().getBillingResponse();
            System.out.println("ding ming ping 01");
            if (mBillingList != null && mBillingList.size() > 0) {
                for (int i = 0; i < mBillingList.size(); i++) {
                    if (mBillingList.get(i).billing_type.equalsIgnoreCase("pro")) {
                        sku_pro = mBillingList.get(i).product_id;

                        // Do we have the premium upgrade?
                        Purchase premiumPurchase = inventory.getPurchase(sku_pro);
//                        System.out.println("112202" + " " + premiumPurchase.getPurchaseTime() + " " + premiumPurchase.getSku() + " " + premiumPurchase.getPurchaseState() + " " + premiumPurchase.getDeveloperPayload() + " " + premiumPurchase.getItemType() + " " + premiumPurchase.getOrderId() + " " + premiumPurchase.getPackageName() + " " + premiumPurchase.getSignature() + " " + premiumPurchase.getToken() + " " + premiumPurchase.isAutoRenewing());
                        mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
                        PrintLog.print("User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
                        mPreference.setPro(mIsPremium);
                        Slave.IS_PRO = mPreference.isPro();
//                        Slave.HAS_PURCHASED = mPreference.isPro();
                        System.out.println("ding ming ping 02" + " " + mPreference.isPro());
                    } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("weekly")) {
                        System.out.println("ding ming ping 03");
                        sku_weekly = mBillingList.get(i).product_id;

                        // Do we have the weekly subscription plan?
                        Purchase weeklyPurchase = inventory.getPurchase(sku_weekly);
                        mSubscribedToWeekly = (weeklyPurchase != null &&
                                verifyDeveloperPayload(weeklyPurchase));
                        mPreference.setWeekly(mSubscribedToWeekly);
                        Slave.IS_WEEKLY = mPreference.isWeekly();
//                        Slave.HAS_PURCHASED = mPreference.isWeekly();
                        System.out.println("ding ming ping 03" + " " + mPreference.isWeekly());
//            System.out.println("112202" + " " + monthlyPurchase.getPurchaseTime() + " " + monthlyPurchase.getSku() + " " + monthlyPurchase.getPurchaseState() + " " + monthlyPurchase.getDeveloperPayload() + " " + monthlyPurchase.getItemType() + " " + monthlyPurchase.getOrderId() + " " + monthlyPurchase.getPackageName() + " " + monthlyPurchase.getSignature() + " " + monthlyPurchase.getToken() + " " + monthlyPurchase.isAutoRenewing());


                    } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("monthly")) {
                        sku_monthly = mBillingList.get(i).product_id;

                        // Do we have the monthly subscription plan?
                        Purchase monthlyPurchase = inventory.getPurchase(sku_monthly);
                        mSubscribedToMonthly = (monthlyPurchase != null &&
                                verifyDeveloperPayload(monthlyPurchase));
                        mPreference.setMonthly(mSubscribedToMonthly);
                        Slave.IS_MONTHLY = mPreference.isMonthly();
//                        Slave.HAS_PURCHASED = mPreference.isMonthly();
                        System.out.println("ding ming ping 04" + " " + mPreference.isMonthly());
                    } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("halfYear")) {
                        sku_halfyaer = mBillingList.get(i).product_id;

                        // Do we have the half yearly subscription plan?
                        Purchase halfYearly = inventory.getPurchase(sku_halfyaer);
                        mSubscribedToHalfYearly = (halfYearly != null &&
                                verifyDeveloperPayload(halfYearly));
                        mPreference.setHalfYearly(mSubscribedToHalfYearly);
                        Slave.IS_HALFYEARLY = mPreference.isHalfYearly();
//                        Slave.HAS_PURCHASED = mPreference.isHalfYearly();
                        System.out.println("ding ming ping 05" + " " + mPreference.isHalfYearly());
                    } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("yearly")) {
                        sku_yearly = mBillingList.get(i).product_id;

                        // Do we have the half yearly subscription plan?
                        Purchase halfYearly = inventory.getPurchase(sku_yearly);
                        mSubscribedToYearly = (halfYearly != null &&
                                verifyDeveloperPayload(halfYearly));
                        mPreference.setYearly(mSubscribedToYearly);
                        Slave.IS_YEARLY = mPreference.isYearly();
//                        Slave.HAS_PURCHASED = mPreference.isYearly();
                        System.out.println("ding ming ping 06" + " " + mPreference.isYearly());
                    }
                }
            }

        }
    };

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }


    // Does the user have the premium upgrade?
    protected boolean mIsPremium = false/*, mIsWeekly = false, mIsMonthly = false, mIsHalfYearly = false, mIsYearly = false*/;

    // Does the user have an active subscription for monthly plan?
    protected boolean mSubscribedToWeekly = false, mSubscribedToMonthly = false, mSubscribedToHalfYearly = false, mSubscribedToYearly = false;
}

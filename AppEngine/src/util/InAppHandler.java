package util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;

import app.PrintLog;
import app.fcm.MapperUtils;
import app.server.v2.Billing;
import app.server.v2.BillingResponseHandler;
import app.server.v2.DataHubConstant;
import app.server.v2.Slave;
import app.inapp.BillingPreference;

/**
 * Created by quantum4u1 on 29/03/18.
 */

public class InAppHandler extends IabHelper {
    // Debug tag, for logging
    private static final String TAG = "Engine Billing Handler";

    private Context mContext;

    private IabHelper mHelper;

    private static final int RC_REQUEST = 9991;

    // Does the user have the premium upgrade?
    private boolean mIsPremium = false/*, mIsWeekly = false, mIsMonthly = false, mIsHalfYearly = false, mIsYearly = false*/;

    // Does the user have an active subscription for monthly plan?
    private boolean mSubscribedToWeekly = false, mSubscribedToMonthly = false, mSubscribedToHalfYearly = false, mSubscribedToYearly = false;

    private BillingPreference mPreference;

    /**
     * Creates an instance. After creation, it will not yet be ready to use. You must perform
     * setup by calling {@link #startSetup} and wait for setup to complete. This constructor does not
     * block and is safe to call from a UI thread.
     *
     * @param ctx             Your application or Activity context. Needed to bind to the in-app billing service.
     * @param base64PublicKey Your application's public key, encoded in base64.
     *                        This is used for verification of purchase signatures. You can find your app's base64-encoded
     *                        public key in your application's page on Google Play Developer Console. Note that this
     */
    public InAppHandler(Context ctx, String base64PublicKey) {
        super(ctx, base64PublicKey);
        this.mContext = ctx;
        mHelper = new IabHelper(ctx, base64PublicKey);
        this.mPreference = new BillingPreference(ctx);
    }

    public IabHelper getIabHelper() {
        return mHelper;
    }


    private void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    private void alert(String message) {
//        AlertDialog.Builder bld = new AlertDialog.Builder((Activity) this.mContext);
//        bld.setMessage(message);
//        bld.setNeutralButton("OK", null);
//        Log.d(TAG, "Showing alert dialog: " + message);
//        bld.create().show();
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    private boolean verifyDeveloperPayload(Purchase p) {
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
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }


    public void initiatePurchase(/*onInitializeListener l,*/ String sku, boolean isSubscription) {
//        this.mOnInitListener = l;
        Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";

        System.out.println("INAPP ding checking 01" + " " + sku);
        if (!isSubscription) {
            System.out.println("INAPP ding checking 02" + " " + sku);
            try {
                mHelper.launchPurchaseFlow((Activity) mContext, sku, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            } catch (IabAsyncInProgressException e) {
                System.out.println("exception inside here pro" + " " + e);
                e.printStackTrace();
            }
        } else {
            System.out.println("INAPP ding checking 03" + " " + sku);
            try {
                mHelper.launchPurchaseFlow((Activity) mContext, sku, IabHelper.ITEM_TYPE_SUBS, null, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            } catch (IabAsyncInProgressException e) {
                System.out.println("exception inside here subs" + " " + e);
                e.printStackTrace();
            }
        }
    }


    public void initializeBilling(/*onInitializeListener l*/) {
//        this.mOnInitListener = l;
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabAsyncInProgressException e) {
                    Log.d(TAG, "Exception onTABSetupFinised" + " " + e);
                    e.printStackTrace();
                }
            }
        });
    }

//    private onInitializeListener mOnInitListener;

//    public interface onInitializeListener {
//        void onInitializationDone(String message);
//
//        void onPurchaseFinishedListener(boolean isPremiun);
//    }

    private QueryInventoryFinishedListener mGotInventoryListener = new QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            String message;
            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                message = "Failed to query inventory";
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");
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
                        Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
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

    private OnIabPurchaseFinishedListener mPurchaseFinishedListener = new OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            System.out.println("inside iab purchase finish 01");

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;


            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }

            System.out.println("inside iab purchase finish 02");
            System.out.println("purchase successful");

            for (Billing b : BillingResponseHandler.getInstance().getBillingResponse()) {
                if (purchase.getSku().equals(b.product_id)) {
                    switch (b.billing_type) {
                        case "pro":
                            alert("Thank you for upgrading to premium!");
                            mIsPremium = true;

                            mPreference.setPro(mIsPremium);
                            Slave.IS_PRO = mPreference.isPro();

//                            mOnInitListener.onPurchaseFinishedListener(mIsPremium);

                            System.out.println("INAPP CHECKS HANDLER 02 " + mIsPremium + " " + Slave.IS_PRO);
                            restartApplication();
                            break;

                        case "weekly":
                            alert("Thank you for upgrading to weekly premium membership");
                            mSubscribedToWeekly = true;

                            mPreference.setWeekly(mSubscribedToWeekly);
                            Slave.IS_WEEKLY = mPreference.isWeekly();


                            System.out.println("INAPP CHECKS HANDLER 04 " + mSubscribedToWeekly + " " + Slave.IS_WEEKLY);
//                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToWeekly);
                            restartApplication();
                            break;

                        case "monthly":
                            alert("Thank you for upgrading to monthly premium membership");
                            mSubscribedToMonthly = true;

                            mPreference.setMonthly(mSubscribedToMonthly);
                            Slave.IS_MONTHLY = mPreference.isMonthly();

                            System.out.println("INAPP CHECKS HANDLER 05 " + mSubscribedToMonthly + " " + Slave.IS_MONTHLY);
//                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToMonthly);
                            restartApplication();
                            break;

                        case "halfYear":
                            alert("Thank you for upgrading to premium!");
                            mSubscribedToHalfYearly = true;

                            mPreference.setHalfYearly(mSubscribedToHalfYearly);
                            Slave.IS_HALFYEARLY = mPreference.isHalfYearly();

                            System.out.println("INAPP CHECKS HANDLER 06 " + mSubscribedToHalfYearly + " " + Slave.IS_HALFYEARLY);
//                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToHalfYearly);
                            restartApplication();
                            break;

                        case "yearly":
                            alert("Thank you for upgrading to premium!");
                            mSubscribedToYearly = true;

                            mPreference.setYearly(mSubscribedToYearly);
                            Slave.IS_YEARLY = mPreference.isYearly();

                            System.out.println("INAPP CHECKS HANDLER 07 " + mSubscribedToYearly + " " + Slave.IS_YEARLY);
//                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToYearly);
                            restartApplication();
                            break;
                        default:
                            alert("dafault case");
                            break;
                    }

                }
            }


           /* String sku_pro, sku_weekly, sku_monthly, sku_halfyaer, sku_yearly;
            ArrayList<Billing> mBillingList = BillingResponseHandler.getInstance().getBillingResponse();
            if (mBillingList != null && mBillingList.size() > 0) {
                for (int i = 0; i < mBillingList.size(); i++) {
                    if (mBillingList.get(i).billing_type.equalsIgnoreCase("pro")) {
                        sku_pro = mBillingList.get(i).product_id;
                        System.out.println("INAPP CHECKS HANDLER 01");
                        if (purchase.getSku().equals(sku_pro)) {

                            Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                            alert("Thank you for upgrading to premium!");
                            mIsPremium = true;

                            mPreference.setPro(mIsPremium);
                            Slave.IS_PRO = mPreference.isPro();

                            mOnInitListener.onPurchaseFinishedListener(mIsPremium);

                            System.out.println("INAPP CHECKS HANDLER 02 " + mIsPremium + " " + Slave.IS_PRO);
                            restartApplication();
                        }

                    } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("weekly")) {
                        sku_weekly = mBillingList.get(i).product_id;
                        System.out.println("INAPP CHECKS HANDLER 03 ");
                        if (purchase.getSku().equals(sku_weekly)) {
                            Log.d(TAG, "Purchase is weekly upgraded. Congratulating user.");
                            alert("Thank you for upgrading to weekly premium membership");
                            mSubscribedToWeekly = true;

                            mPreference.setWeekly(mSubscribedToWeekly);
                            Slave.IS_WEEKLY = mPreference.isWeekly();


                            System.out.println("INAPP CHECKS HANDLER 04 " + mSubscribedToWeekly + " " + Slave.IS_WEEKLY);
                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToWeekly);
                            restartApplication();
                        }

                    } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("monthly")) {
                        sku_monthly = mBillingList.get(i).product_id;

                        if (purchase.getSku().equals(sku_monthly)) {
                            Log.d(TAG, "Purchase is monthly upgraded. Congratulating user.");
                            alert("Thank you for upgrading to monthly premium membership");
                            mSubscribedToMonthly = true;

                            mPreference.setMonthly(mSubscribedToMonthly);
                            Slave.IS_MONTHLY = mPreference.isMonthly();

                            System.out.println("INAPP CHECKS HANDLER 05 " + mSubscribedToMonthly + " " + Slave.IS_MONTHLY);
                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToMonthly);
                            restartApplication();
                        }
                    } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("halfYear")) {
                        sku_halfyaer = mBillingList.get(i).product_id;

                        if (purchase.getSku().equals(sku_halfyaer)) {
                            Log.d(TAG, "Purchase is half yearly upgraded. Congratulating user.");
                            alert("Thank you for upgrading to premium!");
                            mSubscribedToHalfYearly = true;

                            mPreference.setHalfYearly(mSubscribedToHalfYearly);
                            Slave.IS_HALFYEARLY = mPreference.isHalfYearly();

                            System.out.println("INAPP CHECKS HANDLER 06 " + mSubscribedToHalfYearly + " " + Slave.IS_HALFYEARLY);
                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToHalfYearly);
                            restartApplication();
                        }
                    } else if (mBillingList.get(i).billing_type.equalsIgnoreCase("yearly")) {
                        sku_yearly = mBillingList.get(i).product_id;

                        if (purchase.getSku().equals(sku_yearly)) {
                            Log.d(TAG, "Purchase is yearly upgraded. Congratulating user.");
                            alert("Thank you for upgrading to premium!");
                            mSubscribedToYearly = true;

                            mPreference.setYearly(mSubscribedToYearly);
                            Slave.IS_YEARLY = mPreference.isYearly();

                            System.out.println("INAPP CHECKS HANDLER 07 " + mSubscribedToYearly + " " + Slave.IS_YEARLY);
                            mOnInitListener.onPurchaseFinishedListener(mSubscribedToYearly);
                            restartApplication();
                        }
                    }


                }
            }*/


        }
    };

    private void restartApplication() {
        System.out.println("inside restart application");
        PrintLog.print("<<<<here is the initDone 04 bhanu");
        Intent mStartActivity = new Intent(DataHubConstant.CUSTOM_ACTION);
        mStartActivity.putExtra(MapperUtils.keyValue, MapperUtils.LAUNCH_SPLASH);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this.mContext, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) this.mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}


//https://developer.android.com/google/play/billing/billing_reference.html#billing-codes
//https://developer.android.com/google/play/billing/api.html
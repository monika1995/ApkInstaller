package app.server.v2;

import java.util.ArrayList;

/**
 * Created by quantum4u1 on 18/04/18.
 */

public class BillingResponseHandler {
    private static final BillingResponseHandler ourInstance = new BillingResponseHandler();

    public static BillingResponseHandler getInstance() {
        return ourInstance;
    }

    private BillingResponseHandler() {
    }


    private ArrayList<Billing> billingList = new ArrayList<>();


    public ArrayList<Billing> getBillingResponse() {
        return this.billingList;
    }

    public void setBillingResponse(ArrayList<Billing> mBillingList) {
        this.billingList = mBillingList;
    }

}

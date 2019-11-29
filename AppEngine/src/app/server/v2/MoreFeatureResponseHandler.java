package app.server.v2;

import java.util.ArrayList;

/**
 * Created by quantum4u1 on 18/04/18.
 */

public class MoreFeatureResponseHandler {
    private static final MoreFeatureResponseHandler ourInstance = new MoreFeatureResponseHandler();
    private ArrayList<MoreFeature> moreFeaturesList = new ArrayList<>();

    private MoreFeatureResponseHandler() {
    }

    public static MoreFeatureResponseHandler getInstance() {
        return ourInstance;
    }

    public ArrayList<MoreFeature> getMoreFeaturesListResponse() {
        return this.moreFeaturesList;
    }

    void setMoreFeaturesListResponse(ArrayList<MoreFeature> mMoreFeatures) {
        this.moreFeaturesList = mMoreFeatures;
    }

}

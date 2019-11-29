package app.campaign;

public interface ICampaignResponseCallback {
    void onResponseObtained(Object response, int responseType,
                            boolean isCachedData);

    void onErrorObtained(String errormsg, int responseType);
}

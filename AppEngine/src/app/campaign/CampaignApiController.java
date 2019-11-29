package app.campaign;

import android.app.ProgressDialog;
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;

import app.PrintLog;
import app.server.v2.DataHubConstant;
import app.socket.EngineApiController;

public class CampaignApiController implements ICampaignResponseCallback {
    private WeakReference<Context> contxt;
    private ICampaignResponseCallback response;
    private CampaignClient client;


    private static String BASE_URL = EngineApiController.BASE_URL;
    private static String TEST_URL = EngineApiController.TEST_URL;

    private static String ENGINE_VERSION = EngineApiController.ENGINE_VERSION;


    private static String CAMPAIGN_SERVICE = "";

    static final int CAMPAIGN_SERVICE_CODE = 11;


    private int responseType;
    private ProgressDialog dialog;
    private boolean isProgressShow = false;
    private boolean isDataOnline = true;

    private CampaignApiController(Context context, ICampaignResponseCallback response,
                                  int responseType, boolean isProgressShow) {
        this.contxt = new WeakReference<Context>(context);
        this.response = response;
        this.responseType = responseType;
        this.isProgressShow = isProgressShow;

        client = new CampaignClient(contxt.get(), this);


        if (DataHubConstant.IS_LIVE) {
            CAMPAIGN_SERVICE = BASE_URL + "dashboard/newbanners?engv=" + ENGINE_VERSION;
        } else {
            CAMPAIGN_SERVICE = TEST_URL + "dashboard/newbanners?engv=" + ENGINE_VERSION;
        }

    }

    CampaignApiController(Context context, ICampaignResponseCallback response,
                          int responseType) {
        this(context, response, responseType, true);
    }


    void getCampaignRequest(Object mRequest) {
        if (isDataOnline)
            client.Communicate(CAMPAIGN_SERVICE, mRequest, responseType);
    }


    @Override
    public void onResponseObtained(Object response, int responseType,
                                   boolean isCachedData) {
        this.response.onResponseObtained(response, responseType, isCachedData);
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }


    @Override
    public void onErrorObtained(String errormsg, int responseType) {
        this.response.onErrorObtained(errormsg, responseType);
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    public void showProgressDialog() {
        if (isProgressShow)
            dialog = ProgressDialog.show(contxt.get(), "", "");
    }

    public String loadjsonfromAsset(int responseTYpe, Object object) {
//        if (responseTYpe == EngineApiController.CAMPAIGN_DETAIL)
//            return loadJSONFromAsset(contxt.get(), "local_new");
        return null;
    }

    public String loadJSONFromAsset(Context context, String pathName) {
        PrintLog.print("json return is here" + pathName);
        String json = null;
        try {
            InputStream is = context.getAssets().open(pathName + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            PrintLog.print("json retun is here" + json.length());
        } catch (IOException ex) {
            ex.printStackTrace();
            PrintLog.print("json retun is here" + ex);
            return null;
        }
        return json;
    }

}

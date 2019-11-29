package app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import app.PrintLog;
import app.ecrypt.MCrypt;
import app.fcm.GCMPreferences;
import app.pnd.adshandler.R;
import app.rest.response.DataResponse;
import app.server.v2.DataHubConstant;
import app.server.v2.DataHubPreference;
import app.serviceprovider.Utils;

/**
 * Created by quantum4u1 on 05/04/18.
 */

public class PrintActivity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print);
        Intent intent = getIntent();
        textView = findViewById(R.id.textViewPrint);

        if (intent != null) {
            int myShowValue = intent.getIntExtra(Utils.SHOW_VALUE, 0);

            if (myShowValue != 0) {

                if (myShowValue == Utils.SHOW_ASSET_VALUE) {
                    Log.d("PrintActivity", "Hello onCreate show pos 001 " + " " + myShowValue);
                    parseMasterData(new DataHubConstant(this).parseAssetData());

                } else if (myShowValue == Utils.SHOW_SERVER_VALUE) {
                    Log.d("PrintActivity", "Hello onCreate show pos 002 " + " " + myShowValue);

                    String str = new DataHubPreference(this).getJSON();
                    try {
                        textView.setText("Unique ID: " + new GCMPreferences(this).getUniqueId() + "\n"
                                + "Onboard Time: " + new GCMPreferences(this).getFCMRandomOnboard() + "\n"
                                + "Delay Time: " + new GCMPreferences(this).getFCMRandomDelay() + "\n\n"
                                /*new DataHubPreference(this).getJSON()*/
                                + new JSONObject(str).toString(2));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        textView.setTextIsSelectable(true);
        textView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //menu.removeItem(android.R.id.shareText);
                menu.removeItem(android.R.id.cut);

                //MenuInflater inflater = mode.getMenuInflater();
                //inflater.inflate(R.menu.menu_edit_copy, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });


    }

    private void parseMasterData(String response) {
        Gson gson = new Gson();
        MCrypt mCrypt = new MCrypt();
        DataResponse dataResponse;

        if (response != null) {
            dataResponse = gson.fromJson(response, DataResponse.class);
            try {
                String dResponse = new String(mCrypt.decrypt(dataResponse.data));
                parseDecryptMasterData(dResponse);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    private void parseDecryptMasterData(String response) {
        try {
            if (response != null && response.length() > 100) {
                try {
                    textView.setText("Unique ID: " + new GCMPreferences(this).getUniqueId() + "\n"
                            + "Onboard Time: " + new GCMPreferences(this).getFCMRandomOnboard() + "\n"
                            + "Delay Time: " + new GCMPreferences(this).getFCMRandomDelay() + "\n\n"
                            + response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            PrintLog.print("Enginev2 Going to Parse got response message is   6 getting server ads" + response);

        } catch (Exception e) {
            PrintLog.print(" Enginev2 Exception get ad data" + " " + e);

        }


    }

}

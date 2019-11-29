package app.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import app.pnd.adshandler.R;

/**
 * Created by Rakesh Rajput on 24/06/19.
 */
public class ShowAssetValueDialog extends Dialog implements View.OnClickListener {

    ShowAssetValueDialog(Activity a, OnSelecteShowValueCallBack mCallBack) {
        super(a);
        this.mCallBack = mCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.diloag_showvalue);
        TextView show_server_value = findViewById(R.id.show_server_value);
        TextView show_asset_value = findViewById(R.id.show_asset_value);
        ImageView cross_prompt = findViewById(R.id.cross_prompt);

        show_server_value.setOnClickListener(this);
        show_asset_value.setOnClickListener(this);
        cross_prompt.setOnClickListener(this);


    }


    private OnSelecteShowValueCallBack mCallBack;

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.show_asset_value) {
            mCallBack.onShowValueSelected(1);
            cancel();
        } else if (v.getId() == R.id.show_server_value) {
            mCallBack.onShowValueSelected(2);
            cancel();

        } else if (v.getId() == R.id.cross_prompt) {
            cancel();
        }

    }

    public interface OnSelecteShowValueCallBack {
        void onShowValueSelected(int position);
    }


}

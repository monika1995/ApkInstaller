package com.techproof.appinstaller.activity;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import app.adshandler.AHandler;
import app.server.v2.Slave;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View getBanner() {
        /*
         * here ETC_1 value 1 is using for banner adaptive and else is using for banner..
         */
        if (!Slave.ETC_1.equalsIgnoreCase("1")) {

            return AHandler.getInstance().getBannerHeader(this);

        } else {
            return AHandler.getInstance().getBannerFooter(this);

        }
    }
}


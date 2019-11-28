package com.q4u.apkinstaller.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.q4u.apkinstaller.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_more)
    Toolbar toolbarMore;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        ButterKnife.bind(this);
        setSupportActionBar(toolbarMore);

        toolbarMore.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

    }

    @OnClick(R.id.txt_settings)
    public void onClickSettingBtn()
    {
        startActivity(new Intent(this,SettingsActivity.class));
    }
}

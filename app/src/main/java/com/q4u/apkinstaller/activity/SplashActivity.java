package com.q4u.apkinstaller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.q4u.apkinstaller.Common.Constant;
import com.q4u.apkinstaller.R;
import com.q4u.apkinstaller.model.FileListenerService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.txt_start)
    TextView txtStart;
    String path;
    boolean isFromNotification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(getApplicationContext(), FileListenerService.class));
            } else {
                startService(new Intent(getApplicationContext(), FileListenerService.class));
            }

        isFromNotification = getIntent().getBooleanExtra(Constant.FROM_NOTIFICATION,false);
        path = getIntent().getStringExtra(Constant.PATH);

        if(isFromNotification)
        {
            Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
            if (path!=null) {
                intent.putExtra(Constant.APK_PATH, path);
            }
            startActivity(intent);
            finish();
        }
    }

    @OnClick(R.id.txt_start)
    public void onClickStartButton()
    {
        startActivity(new Intent(SplashActivity.this,HomeActivity.class));
        finish();
    }
}

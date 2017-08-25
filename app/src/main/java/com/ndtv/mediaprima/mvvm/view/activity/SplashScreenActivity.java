package com.ndtv.mediaprima.mvvm.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gigya.socialize.android.GSAPI;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;
import com.ndtv.mediaprima.GTM.ContainerHolderSingleton;
import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.ConfigManager;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.mvvm.model.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Elaa on 8/29/2016.
 * modified by nitesh
 */

public class SplashScreenActivity extends AppCompatActivity implements ConfigManager.ConfigListener {

//    private AppUpdater appUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ConfigManager.getsInstance().downloadConfig(SplashScreenActivity.this, SplashScreenActivity.this);
            }
        }, 2000);

    }

    @Override
    public void onResponse(Configuration configuration) {
        if (!isFinishing()) {
            Intent intent = new Intent(this, HomeActivity.class);
            Utility.startActivity(this, intent);
        }
    }

    @Override
    public void onError() {
        if (!isFinishing()) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void initGoogleTagManager(){
        TagManager tagManager = TagManager.getInstance(getApplicationContext());
        tagManager.setVerboseLoggingEnabled(true);
        PendingResult<ContainerHolder> pending =
                tagManager.loadContainerPreferNonDefault(Constants.CONTAINER_ID,
                        R.raw.gtm_mgfvlbd);
        pending.setResultCallback(new ResultCallback<ContainerHolder>() {
            @Override
            public void onResult(ContainerHolder containerHolder) {
                ContainerHolderSingleton.setContainerHolder(containerHolder);
            }
        }, 2, TimeUnit.SECONDS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (GSAPI.getInstance().handleAndroidPermissionsResult(requestCode, permissions, grantResults))
            return;
        // handle other permissions result here
    }
}

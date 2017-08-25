package com.ndtv.mediaprima.mvvm.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.ui.BaseActivity;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.FragmentHelper;
import com.ndtv.mediaprima.mvvm.view.fragment.FullScreenFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.OoyalaVideoPlayerFragment;

/**
 * Created by Elaa on 8/29/2016.
 */

public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        addFragment();
    }

    private void addFragment() {
        Fragment fragment = null;
        Bundle bundle = getIntent().getExtras();
        switch (bundle.getString(Constants.BundleKeys.FRAGMENT_TYPE)) {
            case Constants.SectionType.FULL_SCREEN_IMAGE:
                fragment = new FullScreenFragment();
                break;
            case Constants.SectionType.PLAYER:
                fragment = new OoyalaVideoPlayerFragment();
                break;
        }
        fragment.setArguments(bundle);
        FragmentHelper.addFragment(this, R.id.detail_container, fragment);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
        }

    }

    @Override
    public void setActionBarTitle(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}

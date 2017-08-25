package com.ndtv.mediaprima.common.ui;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.mvvm.model.Data;
import com.ndtv.mediaprima.mvvm.model.SectionItems;

/**
 * Created by Elaa on 8/27/2016.
 */

public abstract class BaseActivity extends AppCompatActivity implements ClickEvents.ListItemListener,
        Constants.ActionBarListener {

    private PublisherAdView publisherAdView;
    private FrameLayout baseContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        baseContainer = (FrameLayout) findViewById(R.id.container);
        publisherAdView = (PublisherAdView) findViewById(R.id.adContainer);
    }

    @Override
    public void onHomeListItemClick(Data item, String sectionType, int position, String actionBarTitle) {

    }

    @Override
    public void onListItemClick(SectionItems item, String fragmentType, int position, String actionBarTitle) {
    }

    @Override
    public void onGalleryItemClick(int position, String actionBarTitle) {
    }

    @Override
    public void setActionBarTitle(Toolbar toolbar, String title) {

    }

    @Override
    public void setCollapsingTitle(CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, String title) {

    }

    protected void setLayout(int layoutId) {
        getLayoutInflater().inflate(layoutId, baseContainer, true);
    }

    protected void showAdContainer() {
        publisherAdView.setVisibility(View.VISIBLE);
    }

    protected void hideAdContainer() {
        publisherAdView.setVisibility(View.GONE);
    }

    protected void loadBannerAds() {
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        publisherAdView.loadAd(adRequest);
    }

}


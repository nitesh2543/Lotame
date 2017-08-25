package com.ndtv.mediaprima.common.utils;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.ndtv.mediaprima.mvvm.model.Banner;
import com.ndtv.mediaprima.mvvm.model.Configuration;
import com.ndtv.mediaprima.mvvm.model.ConfigurationItem;
import com.ndtv.mediaprima.mvvm.model.Data;
import com.ndtv.mediaprima.mvvm.model.Result;
import com.ndtv.mediaprima.mvvm.model.SectionItems;

/**
 * Created by Elaa on 8/27/2016.
 */
public class ClickEvents {
    public interface ListItemListener {
        void onListItemClick(SectionItems item, String sectionType, int position, String actionBarTitle);

        void onHomeListItemClick(Data item, String sectionType, int position, String actionBarTitle);

        void onGalleryItemClick(int position, String actionBarTitle);
    }

    public interface LeftMenuItemListener {
        void onLeftMenuItemClick(ConfigurationItem item, int position, boolean isCloseMenu);
    }

    public interface BannerItemListener {
        void onBannerItemClick(Banner item, String ectionType, String actionBarTitle);
    }

    public interface ToolBarScrollingListener {
        void toolbarScrolling(AppBarLayout appBarLayout, CollapsingToolbarLayout collapsingToolbarLayout,
                              Toolbar toolbar, FrameLayout bannerLayout, boolean scrolling);
    }

    public interface EmptyListListener {
        void onEmptyResponse(boolean isEmpty);
    }
}

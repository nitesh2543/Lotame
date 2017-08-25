package com.ndtv.mediaprima.mvvm.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.exception.InvalidTypeException;
import com.ndtv.mediaprima.mvvm.model.Result;
import com.ndtv.mediaprima.mvvm.model.Sections;
import com.ndtv.mediaprima.mvvm.view.fragment.DramaDetailListFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.DramaDetailSynopsisFragment;

import java.util.List;

/**
 * Created by ELAA on 18-09-2016.
 */
public class DramaDetailPagerAdapter extends FragmentStatePagerAdapter {
    private Context ctx;
    private List<String> summaryList;
    private Result sectionsObject;

    public DramaDetailPagerAdapter(Context ctx, FragmentManager fm, Result object, List<String> summeryList) {
        super(fm);
        this.ctx = ctx;
        this.sectionsObject = object;
        this.summaryList = summeryList;

    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BundleKeys.PARCELABLE_OBJECT, sectionsObject);
        Fragment fragment = null;
        switch (summaryList.get(position)) {
            case Constants.Category.SYNOPSIS:
                fragment = new DramaDetailSynopsisFragment();
                bundle.putString(Constants.BundleKeys.CATEGORY, Constants.Category.SYNOPSIS);
                break;
            case Constants.Category.TRIVIA:
                fragment = new DramaDetailSynopsisFragment();
                bundle.putString(Constants.BundleKeys.CATEGORY, Constants.Category.TRIVIA);
                break;
            case Constants.Category.ARTISTS:
                fragment = new DramaDetailListFragment();
                bundle.putString(Constants.BundleKeys.CATEGORY, Constants.Category.ARTISTS);
                break;
            case Constants.Category.EPISODES:
                fragment = new DramaDetailListFragment();
                bundle.putString(Constants.BundleKeys.CATEGORY, Constants.Category.EPISODES);
                break;
            default:
                throw new InvalidTypeException("Invalid section type");
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return summaryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return summaryList.get(position);
    }
}

package com.ndtv.mediaprima.mvvm.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.FragmentHelper;
import com.ndtv.mediaprima.exception.InvalidTypeException;
import com.ndtv.mediaprima.mvvm.model.Categories;
import com.ndtv.mediaprima.mvvm.view.fragment.ArtistListFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.DramaListFragment;
import com.ndtv.mediaprima.mvvm.view.fragment.LifestyleListFragment;

import java.util.List;

/**
 * Created by ELAA on 20-09-2016.
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private final List<Categories> list;
    private String sectionType;
    private Context ctx;

    public HomePagerAdapter(Context ctx, FragmentManager fm, List<Categories> list, String sectionType) {
        super(fm);
        this.ctx = ctx;
        this.sectionType = sectionType;
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        Categories categories = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleKeys.URL, categories.uri);
        bundle.putString(Constants.BundleKeys.CATEGORY_NAME,categories.name );
        Fragment fragment;

        switch (sectionType) {
            case Constants.SectionType.DRAMA_LIST:
                fragment = new DramaListFragment();
                break;
            case Constants.SectionType.ARTIST_LIST:
                fragment = new ArtistListFragment();
                break;
            case Constants.SectionType.LIFESTYLE_LIST:
                fragment = new LifestyleListFragment();
                break;
            default:
                throw new InvalidTypeException("Invalid section type");
        }

        Fragment drama = FragmentHelper.getFragment((FragmentActivity) ctx, R.id.home_container);
        fragment.setTargetFragment(drama, 10);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).name;
    }
}

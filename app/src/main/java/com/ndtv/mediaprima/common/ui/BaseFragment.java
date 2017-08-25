package com.ndtv.mediaprima.common.ui;


import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.FragmentHelper;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentBaseBinding;
import com.ndtv.mediaprima.mvvm.view.fragment.SearchListFragment;

/**
 * Created by Elaa on 8/27/2016.
 */

public abstract class BaseFragment extends Fragment {

    protected FragmentBaseBinding baseBinding;
    protected SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_base, container, false);
        return baseBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_white, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.parseColor("#CC091A21"));
        searchView.setQueryHint("Carian");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Utility.hideKeyboard(getActivity());
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BundleKeys.SEARCH, query);
                SearchListFragment fragment = new SearchListFragment();
                fragment.setArguments(bundle);
                FragmentHelper.replaceAndAddFragment(getActivity(), R.id.home_container, fragment);
                Utility.hideKeyboard(getContext());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}

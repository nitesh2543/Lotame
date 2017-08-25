package com.ndtv.mediaprima.mvvm.view.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.ui.HomeFragment;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.FragmentHelper;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentLifeStyleHomeBinding;
import com.ndtv.mediaprima.mvvm.model.Categories;
import com.ndtv.mediaprima.mvvm.model.Results;
import com.ndtv.mediaprima.mvvm.view.adapter.HomePagerAdapter;
import com.ndtv.mediaprima.mvvm.viewmodel.DataViewModel;
import com.ndtv.mediaprima.mvvm.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elaa on 8/29/2016.
 */
public class LifestyleHomeFragment extends HomeFragment implements ViewModel.DataListener<Results>, ClickEvents.EmptyListListener {

    private Constants.ActionBarListener actionBarListener;
    private FragmentLifeStyleHomeBinding binding;
    private List<Categories> categoriesList = new ArrayList<>();
    private DataViewModel viewModel;
    private View progressView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionBarListener = (Constants.ActionBarListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_life_style_home, container, false);
        binding.homeContainer1.addView(view);
        progressView = Utility.getProgressBar(getContext());
        binding.root.addView(progressView, Utility.getParams());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Network.isConnected(getActivity())) {
            Network.showAlert(getActivity());
            return;
        }
        setHasOptionsMenu(true);
        actionBarListener.setActionBarTitle(binding.toolbar, getArguments().getString(Constants.BundleKeys.ACTION_BAR_TITLE));
        viewPager.setAdapter(new HomePagerAdapter(getContext(), getChildFragmentManager(), categoriesList, Constants.SectionType.LIFESTYLE_LIST));
        tabLayout.setupWithViewPager(viewPager);
        String url = getArguments().getString(Constants.BundleKeys.URL);
        viewModel = new DataViewModel(this, url);
        viewModel.downloadLifestyleData();
    }

    @Override
    public void onResponse(Results results) {
        if (getContext() != null) {
            categoriesList.clear();
            categoriesList.addAll(results.categories);
            viewPager.getAdapter().notifyDataSetChanged();
            binding.root.removeView(progressView);
            setupTabs(Constants.SectionType.LIFESTYLE_LIST);
        }
        tabLayout.setVisibility((categoriesList.isEmpty()) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onError() {
        binding.emptyView.setVisibility((categoriesList.isEmpty()) ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_white, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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

    @Override
    public void onEmptyResponse(boolean scrolling) {

        AppBarLayout.LayoutParams toolbarLayoutParams = (AppBarLayout.LayoutParams) binding.toolbar.getLayoutParams();
        if (scrolling) {
            toolbarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
            binding.toolbar.setLayoutParams(toolbarLayoutParams);
            CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams)
                    binding.appbarLayout.getLayoutParams();
            appBarLayoutParams.setBehavior(new AppBarLayout.Behavior());
            binding.appbarLayout.setLayoutParams(appBarLayoutParams);

        } else {
            toolbarLayoutParams.setScrollFlags(0);
            binding.toolbar.setLayoutParams(toolbarLayoutParams);
            CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams)
                    binding.appbarLayout.getLayoutParams();
            appBarLayoutParams.setBehavior(null);
            binding.appbarLayout.setLayoutParams(appBarLayoutParams);
        }
    }

}

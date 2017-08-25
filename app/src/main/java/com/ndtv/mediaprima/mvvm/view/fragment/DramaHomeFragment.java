package com.ndtv.mediaprima.mvvm.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.ui.HomeFragment;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentDramaHomeBinding;
import com.ndtv.mediaprima.mvvm.model.Banner;
import com.ndtv.mediaprima.mvvm.model.Categories;
import com.ndtv.mediaprima.mvvm.model.Results;
import com.ndtv.mediaprima.mvvm.view.adapter.HomePagerAdapter;
import com.ndtv.mediaprima.mvvm.viewmodel.DataViewModel;
import com.ndtv.mediaprima.mvvm.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elaa on 8/29/2016.
 * modified by nitesh
 */
public class DramaHomeFragment extends HomeFragment implements ViewModel.DataListener<Results>, ClickEvents.EmptyListListener {

    private Constants.ActionBarListener actionBarListener;
    private String url;
    private List<Categories> categoriesList = new ArrayList<>();
    private FragmentDramaHomeBinding binding;
    private DataViewModel viewModel;
    private ClickEvents.ToolBarScrollingListener toolBarScrollingListener;
    private View progressView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionBarListener = (Constants.ActionBarListener) context;
        toolBarScrollingListener = (ClickEvents.ToolBarScrollingListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drama_home, container, false);
        binding.frameContainer.addView(view);
        binding.bannerContainer.addView(bannerBindingView);
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
        viewPager.setAdapter(new HomePagerAdapter(getContext(), getChildFragmentManager(), categoriesList, Constants.SectionType.DRAMA_LIST));
        tabLayout.setupWithViewPager(viewPager);
        actionBarListener.setCollapsingTitle(binding.collapsingToolbar, binding.toolbar, getArguments().getString(Constants.BundleKeys.ACTION_BAR_TITLE));
        url = getArguments().getString(Constants.BundleKeys.URL);
        viewModel = new DataViewModel(this, url);
        viewModel.downloadData();
    }

    @Override
    public void onResponse(Results results) {
        if (getActivity() != null) {
            List<Banner> list = new ArrayList<>();
            list.clear();
            list.addAll(results.banner);
            setBannerContent(list, true, false);
            categoriesList.clear();
            categoriesList.addAll(results.categories);
            viewPager.getAdapter().notifyDataSetChanged();
            setupTabs(Constants.SectionType.DRAMA_LIST);
            binding.root.removeView(progressView);
        }
        tabLayout.setVisibility((categoriesList.isEmpty()) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onError() {
        binding.emptyView.setVisibility((categoriesList.isEmpty()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEmptyResponse(boolean isEmpty) {
        toolBarScrollingListener.toolbarScrolling(binding.appbarLayout, binding.collapsingToolbar, binding.toolbar,
                binding.bannerContainer, isEmpty);
    }
}

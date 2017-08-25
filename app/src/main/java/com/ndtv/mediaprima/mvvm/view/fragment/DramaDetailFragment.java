package com.ndtv.mediaprima.mvvm.view.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.HomeFragment;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentDramaDetailBinding;
import com.ndtv.mediaprima.mvvm.model.Banner;
import com.ndtv.mediaprima.mvvm.model.Result;
import com.ndtv.mediaprima.mvvm.view.adapter.DramaDetailPagerAdapter;
import com.ndtv.mediaprima.mvvm.viewmodel.DataViewModel;
import com.ndtv.mediaprima.mvvm.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elaa on 8/29/2016.
 */
public class DramaDetailFragment extends HomeFragment implements ViewModel.DataListener<Result> {

    private FragmentDramaDetailBinding binding;
    private Constants.ActionBarListener actionBarListener;
    private List<Banner> bannerList = new ArrayList<>();
    private List<String> categoriesList = new ArrayList<>();
    private DataViewModel viewModel;
    private View progressView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionBarListener = (Constants.ActionBarListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drama_detail, container, false);
        binding.homeContainer.addView(view);
        binding.bannerContainerDetail.addView(bannerBindingView);
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
        String url = getArguments().getString(Constants.BundleKeys.URL);
        String title = getArguments().getString(Constants.BundleKeys.ACTION_BAR_TITLE);
        actionBarListener.setCollapsingTitle(binding.collapsingToolbar, binding.toolbar, title);

        viewModel = new DataViewModel(this, url);
        viewModel.downloadDramaDetail();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResponse(Result response) {
        if (getActivity() != null) {
            categoriesList.clear();

            if (response.data.synopsis.section_content != null)
                categoriesList.add(response.data.synopsis.section_name);
            if (response.data.trivia.section_content != null)
                categoriesList.add(response.data.trivia.section_name);
            if (response.data.artists.section_items.size() != 0)
                categoriesList.add(response.data.artists.section_name);
            if (response.data.episodes.section_items.size() != 0)
                categoriesList.add(response.data.episodes.section_name);

            tabLayout.setVisibility((categoriesList.isEmpty()) ? View.GONE : View.VISIBLE);
            bannerList.clear();
            bannerList.addAll(response.bannerList);
            setBannerContent(bannerList, false, false);

            viewPager.setAdapter(new DramaDetailPagerAdapter(getContext(), getChildFragmentManager(), response, categoriesList));
            tabLayout.setupWithViewPager(viewPager);
            viewPager.getAdapter().notifyDataSetChanged();

            binding.setDrama(response);

            binding.root.removeView(progressView);

            setupTabs(Constants.SectionType.DRAMA_LIST);
        }
    }

    @Override
    public void onError() {
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.DRAMA_DETAIL);
    }
}

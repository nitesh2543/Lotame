package com.ndtv.mediaprima.mvvm.view.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentLifestyleDetailBinding;
import com.ndtv.mediaprima.mvvm.model.Result;
import com.ndtv.mediaprima.mvvm.viewmodel.DataViewModel;
import com.ndtv.mediaprima.mvvm.viewmodel.ViewModel;

/**
 * Created by Elaa on 8/29/2016.
 */
public class LifestyleDetailFragment extends BaseFragment implements ViewModel.DataListener<Result> {

    private FragmentLifestyleDetailBinding binding;
    private Constants.ActionBarListener actionBarListener;
    private DataViewModel viewModel;
    private View progressView;
    private String title, link;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionBarListener = (Constants.ActionBarListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lifestyle_detail, container, false);
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
        actionBarListener.setCollapsingTitle(binding.collapsingToolbar, binding.toolbar,
                getArguments().getString(Constants.BundleKeys.ACTION_BAR_TITLE));
        viewModel = new DataViewModel(this, url);
        viewModel.downloadAboutData();
    }


    @Override
    public void onResponse(Result response) {

        Result result = response;
        binding.setArticle(result);
        title = response.data.name;
        link = response.data.images.get(0);
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Utility.showIntentShare(getActivity(), title, link);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.LIFESTYLE_DETAIL);
    }
}

package com.ndtv.mediaprima.mvvm.view.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.databinding.FragmentAboutBinding;
import com.ndtv.mediaprima.mvvm.model.Result;
import com.ndtv.mediaprima.mvvm.viewmodel.DataViewModel;
import com.ndtv.mediaprima.mvvm.viewmodel.ViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment implements ViewModel.DataListener<Result> {

    private Constants.ActionBarListener actionBarListener;
    private FragmentAboutBinding binding;
    private DataViewModel viewModel;

    public void onAttach(Context context) {
        super.onAttach(context);
        actionBarListener = (Constants.ActionBarListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);
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
        viewModel = new DataViewModel(this, getArguments().getString(Constants.BundleKeys.URL));
        viewModel.downloadAboutData();
        binding.setViewModel(viewModel);
    }

    @Override
    public void onResponse(Result response) {
        if (getActivity() == null)
            return;
        binding.setAbout(response.data);
        GoogleAnalyticsHelper.screenView(getActivity(), response.data.name);
        binding.emptyView.setVisibility((response.data == null) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onError() {
        binding.emptyView.setVisibility(View.VISIBLE);
    }
}

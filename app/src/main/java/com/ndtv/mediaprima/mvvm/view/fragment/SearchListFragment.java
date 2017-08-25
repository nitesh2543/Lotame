package com.ndtv.mediaprima.mvvm.view.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.ConfigManager;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.databinding.FragmentSearchListBinding;
import com.ndtv.mediaprima.mvvm.model.Search;
import com.ndtv.mediaprima.mvvm.view.adapter.SearchListAdapter;
import com.ndtv.mediaprima.mvvm.viewmodel.DataViewModel;
import com.ndtv.mediaprima.mvvm.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchListFragment extends BaseFragment implements ViewModel.DataListener<Search> {

    private Constants.ActionBarListener actionBarListener;
    private FragmentSearchListBinding binding;
    private List<Search.Data> searchList = new ArrayList<>();
    private DataViewModel viewModel;
    private SearchListAdapter adapter;
    private String searchUrl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionBarListener = (Constants.ActionBarListener) context;
        searchUrl = ConfigManager.getsInstance().getSearchUrl(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Network.isConnected(getActivity())) {
            Network.showAlert(getActivity());
            return;
        }

        actionBarListener.setActionBarTitle(binding.toolbar, "Search Result");
        setHasOptionsMenu(true);

        String searchKeyWord = getArguments().getString(Constants.BundleKeys.SEARCH);
        searchUrl = searchUrl + searchKeyWord;

        if ((searchList.size() == 0)) {
            viewModel = new DataViewModel(this, searchUrl);
            viewModel.downloadSearchData();
        } else {
            adapter = new SearchListAdapter(getContext(), searchList);
            binding.recyclerView.setHasFixedSize(true);
            binding.recyclerView.setAdapter(adapter);
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResponse(Search response) {
        searchList.clear();
        searchList.addAll(response.data);
        adapter = new SearchListAdapter(getContext(), searchList);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
        binding.progressBar.setVisibility(View.GONE);
        binding.emptyView.setVisibility((searchList.isEmpty()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onError() {
        binding.emptyView.setVisibility((searchList.isEmpty()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.SEARCH_LIST);
    }

}

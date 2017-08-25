package com.ndtv.mediaprima.common.ui;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.databinding.FragmentRecyclerViewBinding;
import com.ndtv.mediaprima.mvvm.view.fragment.BannerFragment;

/**
 * Created by Elaa on 8/27/2016.
 */
public abstract class RecyclerViewFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    protected FragmentRecyclerViewBinding binding;

    public abstract boolean isLoading();

    public abstract int getTotalItemCount();

    public RecyclerViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycler_view, container, false);
        binding.recyclerView.setHasFixedSize(true);
        binding.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        binding.swipeRefresh.setOnRefreshListener(this);
        return binding.getRoot();
    }

    @Override
    public void onRefresh() {

    }

    protected boolean canLoadMoreItems() {
        if (!Network.isConnected(getActivity()))
            return false;

        RecyclerView.LayoutManager layoutManager = binding.recyclerView.getLayoutManager();
        int visibleItemCount = binding.recyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        boolean isLastItem = totalItemCount == firstVisibleItem + visibleItemCount;
        boolean hasMoreItems = totalItemCount < getTotalItemCount();
        return isLastItem && hasMoreItems && !isLoading();
    }

    protected void setEmptyText(String emptyText) {
        binding.emptyView.setText(emptyText);
    }

    protected void setSwipeRefreshLayout(boolean status) {
        binding.swipeRefresh.setEnabled(status);
    }


    protected void setRefreshing(boolean refreshing) {
        binding.swipeRefresh.setRefreshing(refreshing);
    }

    protected boolean isRefreshing() {
        return binding.swipeRefresh.isRefreshing();
    }

}

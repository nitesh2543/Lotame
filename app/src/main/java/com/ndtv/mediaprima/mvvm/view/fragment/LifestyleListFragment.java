package com.ndtv.mediaprima.mvvm.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.CrowdControlHelper;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.RecyclerViewFragment;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.common.utils.RecyclerViewEvents;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.mvvm.model.Results;
import com.ndtv.mediaprima.mvvm.model.SectionItems;
import com.ndtv.mediaprima.mvvm.view.adapter.ListAdapter;
import com.ndtv.mediaprima.mvvm.viewmodel.DramaViewModel;
import com.ndtv.mediaprima.mvvm.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elaa on 8/29/2016.
 * modified by nitesh
 */
public class LifestyleListFragment extends RecyclerViewFragment implements RecyclerViewEvents.Listener<SectionItems>,
        ViewModel.ResponseListener<Results> {

    private List<SectionItems> list = new ArrayList<>();
    private String url, nextUrl;
    private DramaViewModel viewModel;
    private SectionItems footerView;
    private ClickEvents.ListItemListener listener;
    private int totalItems;
    private ClickEvents.EmptyListListener emptyListListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ClickEvents.ListItemListener) context;
        emptyListListener = (ClickEvents.EmptyListListener) getTargetFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        footerView = new SectionItems(RecyclerViewEvents.FOOTER);
        url = getArguments().getString(Constants.BundleKeys.URL);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (!Network.isConnected(getActivity())) {
            Network.showAlert(getActivity());
            return;
        }
        super.onActivityCreated(savedInstanceState);
        ListAdapter adapter = new ListAdapter(getContext(), list, this, R.layout.adapter_lifestyle_list_item);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (canLoadMoreItems()) {
                    showFooterView();
                    viewModel.downloadNextPage(nextUrl);
                }
            }
        });
        viewModel = new DramaViewModel(this, url);
        binding.setViewModel(viewModel);
        viewModel.downloadDramas();
        binding.setViewModel(viewModel);
    }

    @Override
    public void onItemClick(SectionItems item, View v, int position) {
        if (v.getId() == R.id.share) {
            Utility.showIntentShare(getContext(), item.name, item.image_url);
        } else
            listener.onListItemClick(item, item.type, position, item.name);
    }

    private void showFooterView() {
        if (list.add(footerView))
            binding.recyclerView.getAdapter().notifyItemInserted(list.size() - 1);
    }

    private void hideFooterView() {
        if (list.remove(footerView))
            binding.recyclerView.getAdapter().notifyItemRemoved(list.size());
    }

    @Override
    public boolean isLoading() {
        return viewModel.isNextPageLoading.get();
    }

    @Override
    public int getTotalItemCount() {
        return totalItems;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!Network.isConnected(getActivity())) {
            setRefreshing(false);
            return;
        }
        viewModel.refreshDramas();
    }

    @Override
    public void onResponse(Results results) {
        list.clear();
        list.addAll(results.data);
        binding.recyclerView.getAdapter().notifyDataSetChanged();
        nextUrl = results.meta.pagination.link.nextLink;
        totalItems = results.meta.pagination.total;
        if (emptyListListener != null)
            emptyListListener.onEmptyResponse(list.isEmpty() ? false : true);
        binding.emptyView.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onNextResponse(Results results) {
        hideFooterView();
        int insertPositionStart = list.size();
        list.addAll(results.data);
        binding.recyclerView.getAdapter().notifyItemRangeInserted(insertPositionStart, list.size() - 1);
        nextUrl = results.meta.pagination.link.nextLink;
    }

    @Override
    public void onNextError() {
        hideFooterView();
    }

    @Override
    public void onError() {

    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.LIFESTYLE_LIST);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() == null)
            return;
        if (isVisibleToUser)
            new CrowdControlHelper(getContext(), getArguments().getString(Constants.BundleKeys.CATEGORY_NAME));

    }
}

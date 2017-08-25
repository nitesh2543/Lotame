package com.ndtv.mediaprima.mvvm.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.CrowdControlHelper;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.DividerItemDecorator;
import com.ndtv.mediaprima.common.ui.RecyclerViewFragment;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.common.utils.RecyclerViewEvents;
import com.ndtv.mediaprima.mvvm.model.Results;
import com.ndtv.mediaprima.mvvm.model.SectionItems;
import com.ndtv.mediaprima.mvvm.view.adapter.ListAdapter;
import com.ndtv.mediaprima.mvvm.viewmodel.DramaViewModel;
import com.ndtv.mediaprima.mvvm.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elaa on 8/29/2016.
 */
public class ArtistListFragment extends RecyclerViewFragment implements RecyclerViewEvents.Listener<SectionItems>, ViewModel.ResponseListener<Results> {

    private List<SectionItems> list = new ArrayList<>();
    private DramaViewModel viewModel;
    private SectionItems footerView;
    private ClickEvents.ListItemListener listener;
    private ClickEvents.EmptyListListener emptyListListener;
    private String nextUrl;
    private int totalItems;

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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Network.isConnected(getActivity())) {
            Network.showAlert(getActivity());
            return;
        }
        ListAdapter adapter = new ListAdapter(getContext(), list, this, R.layout.adapter_artist_list_item);
        binding.recyclerView.setLayoutManager(getLayoutManager());
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.home_item_padding);
        binding.recyclerView.addItemDecoration(new DividerItemDecorator(spacingInPixels));
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
        viewModel = new DramaViewModel(this, getArguments().getString(Constants.BundleKeys.URL));
        binding.setViewModel(viewModel);
        viewModel.downloadDramas();
    }

    @Override
    public void onItemClick(SectionItems item, View v, int position) {
        listener.onListItemClick(item, Constants.SectionType.ARTIST_DETAIL, position, item.name);
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (binding.recyclerView.getAdapter().getItemViewType(position)) {
                    case RecyclerViewEvents.FOOTER:
                        return 2;
                    case RecyclerViewEvents.ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }
        });

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return layoutManager;
    }

    private void showFooterView() {
        if (list.add(footerView)) ;
        binding.recyclerView.getAdapter().notifyItemInserted(list.size() - 1);
    }

    private void hideFooterView() {
        if (list.remove(footerView)) ;
        binding.recyclerView.getAdapter().notifyItemRemoved(list.size());
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
        totalItems = results.meta.pagination.total;
        nextUrl = results.meta.pagination.link.nextLink;
        binding.emptyView.setVisibility((list.isEmpty()) ? View.VISIBLE : View.GONE);
        if (emptyListListener != null)
            emptyListListener.onEmptyResponse((list.isEmpty()) ? false : true);

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
        binding.emptyView.setVisibility((list.isEmpty()) ? View.VISIBLE : View.GONE);
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
    public void onResume() {
        super.onResume();
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.ARTIST_LIST);
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

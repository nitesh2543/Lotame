package com.ndtv.mediaprima.mvvm.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.HomeFragment;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.FragmentHelper;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.common.utils.RecyclerViewEvents;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentDramaSangatBinding;
import com.ndtv.mediaprima.mvvm.model.Banner;
import com.ndtv.mediaprima.mvvm.model.Data;
import com.ndtv.mediaprima.mvvm.model.DramaSangat;
import com.ndtv.mediaprima.mvvm.model.SectionItems;
import com.ndtv.mediaprima.mvvm.view.adapter.DramaSangatListAdapter;
import com.ndtv.mediaprima.mvvm.viewmodel.DataViewModel;
import com.ndtv.mediaprima.mvvm.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DramaSangatFragment extends HomeFragment implements ViewModel.DataListener<DramaSangat>, RecyclerViewEvents.Listener<Object> {

    private FragmentDramaSangatBinding fragmentDramaSangatBinding;
    private List<Banner> dramaItems = new ArrayList<>();
    private List<Data> dataList = new ArrayList<>();
    private DataViewModel viewModel;
    private Constants.ActionBarListener actionBarListener;
    private ClickEvents.ListItemListener listItemListener;
    private DramaSangatListAdapter adapter;
    private ClickEvents.ToolBarScrollingListener toolBarScrollingListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionBarListener = (Constants.ActionBarListener) context;
        listItemListener = (ClickEvents.ListItemListener) context;
        toolBarScrollingListener = (ClickEvents.ToolBarScrollingListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentDramaSangatBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_drama_sangat, container, false);
        actionBarListener.setActionBarTitle(fragmentDramaSangatBinding.toolbar, Constants.BundleKeys.EMPTY);
        fragmentDramaSangatBinding.bannerContainer.addView(bannerBindingView);
        return fragmentDramaSangatBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Network.isConnected(getActivity())) {
            Network.showAlert(getActivity());
            return;
        }
        if(getActivity()==null)
            return;
        setHasOptionsMenu(true);
        String url = getArguments().getString(Constants.BundleKeys.URL);
        adapter = new DramaSangatListAdapter(getActivity(), dataList, this);
        fragmentDramaSangatBinding.recyclerView.setHasFixedSize(true);
        fragmentDramaSangatBinding.recyclerView.setAdapter(adapter);
        viewModel = new DataViewModel(this, url);
        if ((dramaItems.size() == 0)) {
            viewModel.downloadHomeData();
        } else {
            fragmentDramaSangatBinding.recyclerView.setHasFixedSize(true);
            fragmentDramaSangatBinding.recyclerView.setAdapter(adapter);
            setBannerContent(dramaItems, true, false);
            fragmentDramaSangatBinding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResponse(DramaSangat response) {
        if (getActivity() != null) {
            dramaItems.clear();
            dataList.clear();

            dramaItems.addAll(response.bannerList);
            setBannerContent(dramaItems, true, false);

            dataList.addAll(response.dataList);
            fragmentDramaSangatBinding.recyclerView.getAdapter().notifyDataSetChanged();
            fragmentDramaSangatBinding.progressBar.setVisibility(View.GONE);
            fragmentDramaSangatBinding.emptyView.setVisibility((dataList.isEmpty()) ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void onError() {
        fragmentDramaSangatBinding.emptyView.setVisibility((dataList.isEmpty()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(Object item, View v, int position) {
        if (item instanceof Data) {
            listItemListener.onHomeListItemClick((Data) item, ((Data) item).sectionType, position, ((Data) item).sectionName);
        }

        if (item instanceof SectionItems) {
            if (v.getId() == R.id.share) {
                Utility.showIntentShare(getContext(), ((SectionItems) item).name, ((SectionItems) item).image_url);
            } else
                listItemListener.onListItemClick((SectionItems) item, ((SectionItems) item).type, position, ((SectionItems) item).name);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        v.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
        searchView.setQueryHint("Carian");
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
    public void onResume() {
        super.onResume();
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.DRAMA_SANGAT);
    }
}


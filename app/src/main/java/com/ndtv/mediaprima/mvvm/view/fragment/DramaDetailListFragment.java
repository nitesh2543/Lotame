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
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.ui.DividerItemDecorator;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.RecyclerViewEvents;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentDramaDetailListBinding;
import com.ndtv.mediaprima.mvvm.model.Result;
import com.ndtv.mediaprima.mvvm.model.SectionItems;
import com.ndtv.mediaprima.mvvm.view.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DramaDetailListFragment extends BaseFragment implements RecyclerViewEvents.Listener<SectionItems> {

    private List<SectionItems> list = new ArrayList<>();
    private List<SectionItems> episodeList = new ArrayList<>();
    private ClickEvents.ListItemListener listener;
    private FragmentDramaDetailListBinding binding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (ClickEvents.ListItemListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drama_detail_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list.clear();
        episodeList.clear();

        Result dramaDetail = getArguments().getParcelable(Constants.BundleKeys.PARCELABLE_OBJECT);
        if (getArguments().getString(Constants.BundleKeys.CATEGORY).equals(Constants.Category.ARTISTS)) {
            list.addAll(dramaDetail.data.artists.section_items);
            binding.recyclerView.setLayoutManager(Utility.setGridLayoutManager(getContext(), 2));
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.home_item_padding);
            binding.recyclerView.addItemDecoration(new DividerItemDecorator(spacingInPixels));
            ListAdapter adapter = new ListAdapter(getContext(), list, this, R.layout.adapter_artist_list_item);
            binding.recyclerView.setAdapter(adapter);
        }
        if (getArguments().getString(Constants.BundleKeys.CATEGORY).equals(Constants.Category.EPISODES)) {
            episodeList.addAll(dramaDetail.data.episodes.section_items);
            binding.recyclerView.setLayoutManager(Utility.setVerticalLayoutManager(getContext()));
            ListAdapter adapter = new ListAdapter(getContext(), episodeList, this, R.layout.adapter_drama_detail_episode_item);
            binding.recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onItemClick(SectionItems item, View v, int position) {
        Utility.hideKeyboard(getActivity());
        listener.onListItemClick(item, item.type, position, item.name);
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.DRAMA_DETAIL_LIST);
    }
}

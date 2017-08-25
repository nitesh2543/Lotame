package com.ndtv.mediaprima.mvvm.view.fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.GoogleAnalyticsHelper;
import com.ndtv.mediaprima.common.ui.DividerItemDecorator;
import com.ndtv.mediaprima.common.ui.HomeFragment;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Network;
import com.ndtv.mediaprima.common.utils.RecyclerViewEvents;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.FragmentArtistDetailBinding;
import com.ndtv.mediaprima.mvvm.model.ArtistProfile;
import com.ndtv.mediaprima.mvvm.model.Banner;
import com.ndtv.mediaprima.mvvm.model.Result;
import com.ndtv.mediaprima.mvvm.model.SectionItems;
import com.ndtv.mediaprima.mvvm.view.adapter.ListAdapter;
import com.ndtv.mediaprima.mvvm.viewmodel.DataViewModel;
import com.ndtv.mediaprima.mvvm.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistDetailFragment extends HomeFragment implements ViewModel.DataListener<Result>, RecyclerViewEvents.Listener<SectionItems> {

    private FragmentArtistDetailBinding binding;
    private Constants.ActionBarListener actionBarListener;
    private ClickEvents.ListItemListener listItemListener;
    private DataViewModel viewModel;
    private List<Banner> bannerList = new ArrayList<>();
    private View progressView;
    private String title, link;
    private Result data;
    public static List<SectionItems> sectionItems = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionBarListener = (Constants.ActionBarListener) context;
        listItemListener = (ClickEvents.ListItemListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_artist_detail, container, false);
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
        actionBarListener.setCollapsingTitle(binding.collapsingToolbar, binding.toolbar, getArguments().getString(Constants.BundleKeys.ACTION_BAR_TITLE));
        viewModel = new DataViewModel(this, getArguments().getString(Constants.BundleKeys.URL));
        viewModel.downloadAboutData();
    }

    @Override
    public void onResponse(Result response) {
        if (getActivity() != null) {
            binding.root.removeView(progressView);
            bannerList.clear();

            binding.setArtist(response);
            bannerList.addAll(response.bannerList);
            setBannerContent(bannerList, false, true);
            link = bannerList.get(0).image_url;

            List<ArtistProfile> list = new ArrayList<>();
            list.addAll(response.data.description.artist_profile);
            addArtistProfileDetails(list);

            int j = 0;
            List<SectionItems> dramaList = new ArrayList<>();
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.home_item_padding);
            int resId = 0;
            RecyclerView view = null;
            while (j < 3) {
                switch (j) {
                    case 0:
                        dramaList = response.data.dramas.section_items;
                        resId = R.layout.adapter_artist_detail_drama_item;
                        binding.recyclerView.setLayoutManager(Utility.setHorizondalLayoutManager(getContext()));
                        binding.recyclerView.addItemDecoration(new DividerItemDecorator(spacingInPixels));
                        view = binding.recyclerView;
                        binding.starredInTitle.setVisibility((dramaList.isEmpty()) ? view.GONE : view.VISIBLE);
                        binding.recyclerView.setVisibility((dramaList.isEmpty()) ? view.GONE : view.VISIBLE);
                        break;
                    case 1:
                        dramaList = response.data.articles.section_items;
                        resId = R.layout.adapter_artist_detail_article_item;
                        view = binding.recyclerViewLifestyle;
                        binding.articleTitle.setVisibility((response.data.articles.section_items.isEmpty()) ? view.GONE : view.VISIBLE);
                        binding.recyclerViewLifestyle.setVisibility((response.data.articles.section_items.isEmpty()) ? view.GONE : view.VISIBLE);
                        break;
                    case 2:
                        dramaList = response.data.gallery.section_items;
                        resId = R.layout.adapter_artist_detail_gallery_item;
                        view = binding.recyclerViewGallery;
                        binding.recyclerViewGallery.setLayoutManager(Utility.setHorizondalLayoutManager(getContext()));
                        binding.recyclerViewGallery.addItemDecoration(new DividerItemDecorator(spacingInPixels));
                        binding.galleryTitle.setVisibility((dramaList.isEmpty()) ? view.GONE : view.VISIBLE);
                        binding.recyclerViewGallery.setVisibility((dramaList.isEmpty()) ? view.GONE : view.VISIBLE);
                        break;
                }
                setarticleListAdapter(view, dramaList, resId);
                j++;
            }
            title = response.data.name;
            /*if (!response.data.gallery.section_items.isEmpty())
                link = response.data.gallery.section_items.get(0).image_url;*/

            if (response.data.trivia.section_content != null) {
                binding.triviaTitle.setVisibility(View.VISIBLE);
                binding.triviaDescription.setVisibility(View.VISIBLE);
            } else {
                binding.triviaTitle.setVisibility(View.GONE);
                binding.triviaDescription.setVisibility(View.GONE);
            }
            data = response;

            sectionItems.clear();
            sectionItems.addAll(response.data.gallery.section_items);

            binding.emptyView.setVisibility((response.equals(null)) ? View.VISIBLE : View.GONE);
        }
    }

    private void setarticleListAdapter(RecyclerView view, List list, int i) {
        ListAdapter adapter1 = new ListAdapter(getContext(), list, this, i);
        view.setAdapter(adapter1);
    }

    @Override
    public void onError() {
        binding.emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(SectionItems item, View v, int position) {
        if (v.getId() == R.id.share) {
            Utility.showIntentShare(getContext(), item.name, item.image_url);
        } else {
            if (!item.type.equals(Constants.SectionType.GALLERY)) {
                listItemListener.onListItemClick(item, item.type, position, item.name);
            } else {
                listItemListener.onGalleryItemClick(position, getArguments().getString(Constants.BundleKeys.ACTION_BAR_TITLE));
            }

        }
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
                String shareUrl = getArguments().getString(Constants.BundleKeys.SHARE_IMAGE);
                if (shareUrl != null)
                    Utility.showIntentShare(getContext(), title, shareUrl);
                else
                    Utility.showIntentShare(getContext(), title, link);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addArtistProfileDetails(List<ArtistProfile> list) {
        binding.artistNameTitle.setText(list.get(0).section_name);
        binding.artistName.setText(list.get(0).section_content);

        binding.artistGenderTitle.setText(list.get(1).section_name);
        binding.artistGender.setText(list.get(1).section_content);

        binding.artistNickNameTitle.setText(list.get(2).section_name);
        binding.artistNickName.setText(list.get(2).section_content);

        binding.artistAgeTitle.setText(list.get(3).section_name);
        binding.artistAge.setText(list.get(3).section_content);

        binding.artistDobTitle.setText(list.get(4).section_name);
        binding.artistDob.setText(list.get(4).section_content);

        binding.artistHometownTitle.setText(list.get(5).section_name);
        binding.artistHometown.setText(list.get(5).section_content);

        binding.artistAchievementTitle.setText(list.get(6).section_name);
        binding.artistAchievement.setText(list.get(6).section_content);

        binding.artistJoinTitle.setText(list.get(7).section_name);
        binding.artistJoin.setText(list.get(7).section_content);
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleAnalyticsHelper.screenView(getActivity(), Constants.DramaSangatScreenName.ARTIST_DETAIL);
    }
}

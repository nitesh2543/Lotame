package com.ndtv.mediaprima.mvvm.view.fragment;


import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.databinding.FragmentFullScreenBinding;
import com.ndtv.mediaprima.mvvm.model.SectionItems;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullScreenFragment extends BaseFragment implements View.OnClickListener,
        View.OnTouchListener, ViewPager.OnPageChangeListener {

    private List<SectionItems> galleryList = new ArrayList<>();
    private FragmentFullScreenBinding binding;
    private int position;
    private Handler handler;
    private Runnable runnable;
    private Constants.ActionBarListener actionBarListener;
    private ObservableBoolean isPortrait = new ObservableBoolean(true);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        actionBarListener = (Constants.ActionBarListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_full_screen, container, false);
        binding.leftArrow.setOnClickListener(this);
        binding.rightArrow.setOnClickListener(this);
        actionBarListener.setActionBarTitle(binding.toolbar, getArguments().getString(Constants.BundleKeys.ACTION_BAR_TITLE));
        binding.setIsPortrait(isPortrait);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        position = getArguments().getInt(Constants.BundleKeys.POSITION);

        galleryList = ArtistDetailFragment.sectionItems;
        GalleryPagerAdapter adapter = new GalleryPagerAdapter(getContext(), galleryList);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(position, true);
        binding.leftArrow.setOnTouchListener(this);
        binding.rightArrow.setOnTouchListener(this);
        binding.viewPager.addOnPageChangeListener(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                binding.leftArrow.setVisibility(View.GONE);
                binding.rightArrow.setVisibility(View.GONE);
            }
        };
        visbilityOfButton();
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        isPortrait.set((newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) ? false : true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_arrow:
                if (position != 0) {
                    position--;
                    binding.viewPager.setCurrentItem(position, true);
                }
                break;
            case R.id.right_arrow:
                if (galleryList.size() - 1 != position) {
                    position++;
                    binding.viewPager.setCurrentItem(position, true);
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideViewsDelay();
        return false;
    }

    private void hideViewsDelay() {
        visbilityOfButton();
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 3000);
    }

    private void visbilityOfButton() {
        binding.leftArrow.setVisibility((position == 0) ? View.GONE : View.VISIBLE);
        binding.rightArrow.setVisibility((position == galleryList.size() - 1) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
        visbilityOfButton();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public class GalleryPagerAdapter extends PagerAdapter {

        private List<SectionItems> list;
        private Context context;

        public GalleryPagerAdapter(Context context, List<SectionItems> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.adapter_gallery_item, container, false);
            container.addView(view);
            ImageViewTouch imageView = (ImageViewTouch) view.findViewById(R.id.gallery_item);
            imageView.setOnTouchListener(FullScreenFragment.this);
            Picasso.with(context).load(list.get(position).fullImage).
                    placeholder(context.getResources().getDrawable(R.drawable.place_holder_banner)).into(imageView);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}

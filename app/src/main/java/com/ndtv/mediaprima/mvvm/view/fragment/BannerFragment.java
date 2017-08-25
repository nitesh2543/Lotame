package com.ndtv.mediaprima.mvvm.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.custom.CircularRevealAnimation;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.databinding.FragmentBannerBinding;
import com.ndtv.mediaprima.mvvm.model.Banner;
import com.ndtv.mediaprima.mvvm.view.adapter.PagerIndicatorAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class BannerFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private FragmentBannerBinding fragmentBannerBinding;
    private List<Banner> list = new ArrayList<>();
    private List<Integer> colorsActive = new ArrayList<>();
    private List<Integer> colorsInActive = new ArrayList<>();
    private TextView[] dots;
    private int currentPosition;
    private ClickEvents.BannerItemListener bannerItemListener;
    private boolean isClickDetail;
    private int currentPage;
    private Timer timer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        bannerItemListener = (ClickEvents.BannerItemListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBannerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_banner, container, false);
        return fragmentBannerBinding.getRoot();
    }

    protected void setBannerContent(List<Banner> list, boolean isClickDetail, boolean subTitle) {
        this.list = list;
        this.isClickDetail = isClickDetail;
        if (list.size() == 0) {
            fragmentBannerBinding.mainLayout.setVisibility(View.GONE);
        } else {

            if (list.size() == 1) {
                fragmentBannerBinding.indicator.setVisibility(View.INVISIBLE);
            }
            fragmentBannerBinding.mainLayout.setVisibility(View.VISIBLE);
            fragmentBannerBinding.viewpager.setAdapter(new PagerIndicatorAdapter(getContext(), list, this, subTitle));
            fragmentBannerBinding.viewpager.setOnPageChangeListener(this);
            addBottomDots(0);
            CircularRevealAnimation.calculateRevealDimension(fragmentBannerBinding.circularRevealFrame);
            if (list.size() > 1)
                autoScrollItem(list.size());
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[list.size()];
        fragmentBannerBinding.indicator.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            if (getActivity() != null) {
                colorsActive.add(R.color.white);
                colorsInActive.add(android.R.color.darker_gray);
                dots[i] = new TextView(getContext());
                dots[i].setText(Html.fromHtml("&#8226;"));
                dots[i].setTextSize(30);
                dots[i].setTextColor(getContext().getResources().getColor(colorsInActive.get(currentPage)));
                fragmentBannerBinding.indicator.addView(dots[i]);
            }

        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getContext().getResources().getColor(colorsActive.get(currentPage)));
    }

    private void autoScrollItem(final int itemSize) {

        if (getActivity() == null)
            return;

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == itemSize - 1) {
                    currentPage = 0;
                } else
                    currentPage++;
                fragmentBannerBinding.viewpager.setCurrentItem(currentPage, true);

            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 3000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view:
                if (!(list.get(currentPosition).banner_type.equals(Constants.BundleKeys.VIDEO))) {
                    if (isClickDetail) {
                        bannerItemListener.onBannerItemClick(list.get(currentPosition), list.get(currentPosition).content_type, list.get(currentPosition).name);
                    }
                } else {
                    bannerItemListener.onBannerItemClick(list.get(currentPosition), Constants.SectionType.PLAYER, list.get(currentPosition).name);

                }


                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        addBottomDots(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null)
            timer.cancel();
    }
}

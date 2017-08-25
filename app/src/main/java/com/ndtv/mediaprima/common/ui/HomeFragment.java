package com.ndtv.mediaprima.common.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.mvvm.view.fragment.BannerFragment;

/**
 * Created by Elaa on 8/27/2016.
 */

public abstract class HomeFragment extends BannerFragment {

    //    protected FragmentHomeBinding fragmentHomeBinding;
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected View bannerBindingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        bannerBindingView = super.onCreateView(inflater, container, savedInstanceState);
        return fragmentHomeBinding.getRoot();*/
        bannerBindingView = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        return view;
    }

    protected void setupTabs(String fragment) {
        int color = 0;
        if (fragment.equals(Constants.SectionType.DRAMA_LIST)) {
            color = R.color.tab_selected_text_color;
        } else {
            color = R.color.lifestyle_tab_selected;
        }
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(color));
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null && tab.getCustomView() == null) {
                TextView tabTextView = new TextView(getContext());
                tab.setCustomView(tabTextView);
                tabTextView.setText(tab.getText());
                tabTextView.setAllCaps(true);
                tabTextView.setGravity(Gravity.CENTER);

                // First tab is the selected tab, so if i==0 then set BOLD typeface
                if (i == tabLayout.getSelectedTabPosition()) {
                    tabTextView.setTypeface(null, Typeface.BOLD);
                    tabTextView.setTextColor(getResources().getColor(color));
                } else {
                    tabTextView.setTextColor(getResources().getColor(R.color.lifestyle_tab_text));
                }
            }
        }
        final int finalColor = color;
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Utility.hideKeyboard(getActivity());
                viewPager.setCurrentItem(tab.getPosition());
                TextView text = (TextView) tab.getCustomView();
                if (text != null) {
                    Log.d("TAG", "onTabSelected: ");
                    text.setTextColor(getResources().getColor(finalColor));
                    text.setTypeface(null, Typeface.BOLD);
                    text.setGravity(Gravity.CENTER);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                TextView text = (TextView) tab.getCustomView();
                if (text != null) {
                    Log.d("TAG", "onTabUnselected: ");
                    text.setTypeface(null, Typeface.NORMAL);
                    text.setTextColor(getResources().getColor(R.color.lifestyle_tab_text));
                    text.setGravity(Gravity.CENTER);

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

    }
}

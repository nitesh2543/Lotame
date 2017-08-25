package com.ndtv.mediaprima.mvvm.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.ui.BaseFragment;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.databinding.FragmentDramaDetailSynopsisBinding;
import com.ndtv.mediaprima.mvvm.model.Result;

/**
 * A simple {@link Fragment} subclass.
 */
public class DramaDetailSynopsisFragment extends BaseFragment {

    private FragmentDramaDetailSynopsisBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drama_detail_synopsis, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String category = getArguments().getString(Constants.BundleKeys.CATEGORY);
        Result dramaDetail = getArguments().getParcelable(Constants.BundleKeys.PARCELABLE_OBJECT);

        if (category.equals(Constants.Category.TRIVIA)) {
            String triviaContent = dramaDetail.data.trivia.section_content;
            if (triviaContent != null)
                binding.description.setText(Html.fromHtml(triviaContent));
        }
        if (category.equals(Constants.Category.SYNOPSIS)) {
            String sectionContent = dramaDetail.data.synopsis.section_content;
            if (sectionContent != null)
                binding.description.setText(Html.fromHtml(sectionContent));
        }
    }
}


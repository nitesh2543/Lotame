package com.ndtv.mediaprima.common.utils;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Elaa on 8/28/2016.
 */
public class RecyclerViewHolder <T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private final T binding;

    public RecyclerViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public T getBinding() {
        return binding;
    }
}

package com.ndtv.mediaprima.mvvm.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ndtv.mediaprima.BR;
import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.ui.DividerItemDecorator;
import com.ndtv.mediaprima.common.utils.RecyclerViewEvents;
import com.ndtv.mediaprima.common.utils.RecyclerViewHolder;
import com.ndtv.mediaprima.common.utils.RecyclerViewOnItemClickHandler;
import com.ndtv.mediaprima.common.utils.Utility;
import com.ndtv.mediaprima.databinding.AdapterHomePageParentItemBinding;
import com.ndtv.mediaprima.mvvm.model.Data;
import com.ndtv.mediaprima.mvvm.model.SectionItems;

import java.util.List;

/**
 * Created by Elaa on 8/31/2016.
 */
public class DramaSangatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater layoutInflater;
    private final List<Data> list;
    private RecyclerViewEvents.Listener<?> listener;
    private Context context;
    private AdapterHomePageParentItemBinding parentItemBinding;

    public DramaSangatListAdapter(Context context, List<Data> list, RecyclerViewEvents.Listener<Object> listener) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parentItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_home_page_parent_item, parent, false);
        return new RecyclerViewHolder<>(parentItemBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Data item = list.get(position);
        RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
        viewHolder.getBinding().setVariable(BR.parent_item, item);
        viewHolder.getBinding().setVariable(BR.clickListener, new RecyclerViewOnItemClickHandler<>(item, position, listener));
        parentItemBinding.recyclerView.setHasFixedSize(true);
        String sectionType = null;
        int spacingInPixels;
        int resId;

        switch (position) {
            case 2:
                parentItemBinding.recyclerView.setLayoutManager(Utility.setVerticalLayoutManager(context));
                resId = R.layout.adapter_lifestyle_list_item;
                parentItemBinding.title.setTextColor(context.getResources().getColor(R.color.lifestyle_text_color));
                break;
            case 1:
                parentItemBinding.recyclerView.setLayoutManager(Utility.setGridLayoutManager(context, 2));
                spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.home_item_padding);
                parentItemBinding.recyclerView.addItemDecoration(new DividerItemDecorator(spacingInPixels));
                resId = R.layout.adapter_artist_list_item;
                parentItemBinding.title.setTextColor(context.getResources().getColor(R.color.artist_text_color));
                break;
            default:
                parentItemBinding.recyclerView.setLayoutManager(Utility.setGridLayoutManager(context, 2));
                spacingInPixels = context.getResources().getDimensionPixelSize(R.dimen.home_item_padding);
                parentItemBinding.recyclerView.addItemDecoration(new DividerItemDecorator(spacingInPixels));
                resId = R.layout.adapter_drama_list_item;
                parentItemBinding.title.setTextColor(context.getResources().getColor(R.color.drama_text_color));
                break;
        }
        ListAdapter adapter = new ListAdapter(context, list.get(position).sectionItems, (RecyclerViewEvents.Listener<SectionItems>) listener, resId);
        parentItemBinding.recyclerView.setAdapter(adapter);
        viewHolder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
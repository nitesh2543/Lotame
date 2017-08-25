package com.ndtv.mediaprima.mvvm.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ndtv.mediaprima.BR;
import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.utils.FooterViewHolder;
import com.ndtv.mediaprima.common.utils.RecyclerViewEvents;
import com.ndtv.mediaprima.common.utils.RecyclerViewHolder;
import com.ndtv.mediaprima.common.utils.RecyclerViewOnItemClickHandler;
import com.ndtv.mediaprima.mvvm.model.SectionItems;

import java.util.List;

/**
 * Created by ELAA on 20-09-2016.
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<SectionItems> list;
    private RecyclerViewEvents.Listener<SectionItems> listener;
    private int resId;

    public ListAdapter(Context context, List<SectionItems> list, RecyclerViewEvents.Listener<SectionItems> listener, int resId) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.listener = listener;
        this.resId = resId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case RecyclerViewEvents.FOOTER:
                return new FooterViewHolder(layoutInflater.inflate(R.layout.footer_progress_bar, parent, false));

            default:
                ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, resId, parent, false);
                return new RecyclerViewHolder<>(binding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            SectionItems item = list.get(position);
            RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
            viewHolder.getBinding().setVariable(BR.child_item, item);
            viewHolder.getBinding().setVariable(BR.clickListener, new RecyclerViewOnItemClickHandler<>(item, position, listener));
            viewHolder.getBinding().executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        SectionItems item = list.get(position);
        return item.viewType;
    }
}

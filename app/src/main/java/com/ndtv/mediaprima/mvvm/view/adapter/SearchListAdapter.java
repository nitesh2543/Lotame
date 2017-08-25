package com.ndtv.mediaprima.mvvm.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.utils.ClickEvents;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.mvvm.model.Search;
import com.ndtv.mediaprima.mvvm.model.SectionItems;

import java.util.List;

/**
 * Created by ELAA on 18-09-2016.
 */
public class SearchListAdapter extends ExpandableRecyclerAdapter<SearchListAdapter.ParentHolder, SearchListAdapter.ChildHolder> {
    private final LayoutInflater layoutInflater;
    private Context context;
    private ClickEvents.ListItemListener listener;

    public SearchListAdapter(Context context, List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        listener = (ClickEvents.ListItemListener) context;
    }

    @Override
    public ParentHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View header = layoutInflater.inflate(R.layout.adapter_search_list_parent, parentViewGroup, false);
        return new ParentHolder(header);
    }

    @Override
    public ChildHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View child = layoutInflater.inflate(R.layout.adapter_search_list_child, childViewGroup, false);
        return new ChildHolder(child);
    }

    @Override
    public void onBindParentViewHolder(ParentHolder parentViewHolder, int position, ParentListItem parentListItem) {
        Search.Data item = (Search.Data) parentListItem;
        switch (item.section_type) {
            case Constants.SectionType.ARTIST_LIST:
                parentViewHolder.searchResultHeader.setTextColor(context.getResources().getColor(R.color.artist_text_color));
                break;
            case Constants.SectionType.DRAMA_LIST:
                parentViewHolder.searchResultHeader.setTextColor(context.getResources().getColor(R.color.drama_text_color));
                break;
            case Constants.SectionType.LIFESTYLE_LIST:
                parentViewHolder.searchResultHeader.setTextColor(context.getResources().getColor(R.color.lifestyle_text_color));
                break;
        }

        parentViewHolder.bind(item);
    }

    @Override
    public void onBindChildViewHolder(ChildHolder childViewHolder, final int position, Object childListItem) {
        final SectionItems item = (SectionItems) childListItem;
        childViewHolder.bind(item);
        childViewHolder.childTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.type != null)
                    listener.onListItemClick(item, item.type, position, item.name);
            }
        });
    }

    public class ParentHolder extends ParentViewHolder {

        private TextView searchResultHeader;

        public ParentHolder(View itemView) {
            super(itemView);
            searchResultHeader = (TextView) itemView.findViewById(R.id.parent_textview);
            searchResultHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bind(Search.Data recipe) {
            searchResultHeader.setText(recipe.section_name);
        }
    }

    public class ChildHolder extends ChildViewHolder {

        private TextView childTextView;

        public ChildHolder(View itemView) {
            super(itemView);
            childTextView = (TextView) itemView.findViewById(R.id.child_textview);
        }

        public void bind(SectionItems item) {
            childTextView.setText(item.name);
        }
    }
}

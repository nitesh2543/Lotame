package com.ndtv.mediaprima.mvvm.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.mvvm.model.ConfigurationItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ELAA on 23-09-2016.
 */
public class MenuAdapter extends BaseAdapter {

    private int selectedPos = -1;
    private List<ConfigurationItem> list;
    private Context context;
    private LayoutInflater inflater;

    public MenuAdapter(Context context, List<ConfigurationItem> list) {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_menu_list, parent, false);
            viewHolder = new ListViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListViewHolder) convertView.getTag();
        }
        if (selectedPos == position)
            viewHolder.llItem.setBackgroundColor((context.getResources().getColor(R.color.menu_selector)));
        else
            viewHolder.llItem.setBackgroundColor(Color.parseColor("#00000000"));
        ConfigurationItem menuItem = (ConfigurationItem) getItem(position);

        viewHolder.textView.setText(menuItem.name);

        if (position < (list.size() - 1) && !(list.get(position).group.equals(list.get(position + 1).group)))
            viewHolder.divider.setVisibility(View.VISIBLE);

        switch (menuItem.type) {
            case Constants.SectionType.DRAMA_LIST:
                viewHolder.textView.setTextColor(context.getResources().getColor(R.color.drama_text_color));
                break;
            case Constants.SectionType.ARTIST_LIST:
                viewHolder.textView.setTextColor(context.getResources().getColor(R.color.artist_text_color));
                break;
            case Constants.SectionType.LIFESTYLE_LIST:
                viewHolder.textView.setTextColor(context.getResources().getColor(R.color.lifestyle_text_color));
                break;
            default:
                viewHolder.textView.setTextColor(context.getResources().getColor(R.color.white));
                break;
        }

        if (menuItem.icon == null || menuItem.icon == "") {
            switch (menuItem.type) {
                case Constants.SectionType.RECOMMEND_APP:
                    viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icn_recommend));
                    break;
                case Constants.SectionType.LOGIN:
                    viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icn_login));
                    break;
                case Constants.SectionType.LOGOUT:
                    viewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.icn_login));
                    break;
            }
        } else
            Picasso.with(context).load(menuItem.icon).into(viewHolder.imageView);

        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setSelectedPosition(int pos) {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPos;
    }

    private class ListViewHolder {
        public TextView textView;
        public ImageView imageView;
        public LinearLayout llItem;
        private View divider;

        public ListViewHolder(View item) {
            textView = (TextView) item.findViewById(R.id.menu_item);
            imageView = (ImageView) item.findViewById(R.id.image_view);
            llItem = (LinearLayout) item.findViewById(R.id.llItem);
            divider = item.findViewById(R.id.divider);
        }
    }
}

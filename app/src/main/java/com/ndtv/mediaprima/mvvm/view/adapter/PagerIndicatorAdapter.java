package com.ndtv.mediaprima.mvvm.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ndtv.mediaprima.R;
import com.ndtv.mediaprima.common.utils.Constants;
import com.ndtv.mediaprima.mvvm.model.Banner;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Elaa on 8/28/2016.
 */
public class PagerIndicatorAdapter extends PagerAdapter {

    private final List<Banner> list;
    private final LayoutInflater layoutInflater;
    private View.OnClickListener listener;
    private Context context;
    private boolean subtitle;

    public PagerIndicatorAdapter(Context ctx, List<Banner> list, View.OnClickListener listener, boolean subtitle) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
        this.list = list;
        this.context = ctx;
        this.subtitle = subtitle;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.adapter_help_pager_item, container, false);
        ImageView helpPagerImage = (ImageView) itemView.findViewById(R.id.image_view);
        ImageView videoThumbImage = (ImageView) itemView.findViewById(R.id.thumb);
        LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.banner_title);
        TextView textView = (TextView) itemView.findViewById(R.id.title);
        TextView categoryTitle = (TextView) itemView.findViewById(R.id.category_type);
        ImageView artistIcnStar = (ImageView) itemView.findViewById(R.id.icn_artist_star);

        Banner item = list.get(position);
        switch (item.content_type) {
            case Constants.SectionType.DRAMA_DETAIL:
                layout.setBackgroundColor(context.getResources().getColor(R.color.drama_text_background));
                artistIcnStar.setVisibility(View.GONE);
                categoryTitle.setText(item.category_name);
                break;
            case Constants.SectionType.ARTIST_DETAIL:
                layout.setBackgroundColor(context.getResources().getColor(R.color.artist_text_background));
                categoryTitle.setVisibility(View.VISIBLE);
                categoryTitle.setText(context.getString(R.string.Artist));
                artistIcnStar.setVisibility(View.GONE);
                if (subtitle) {
                    categoryTitle.setVisibility(View.VISIBLE);
                    artistIcnStar.setVisibility(View.GONE);
                    categoryTitle.setText(context.getString(R.string.Artist));
                }

                break;
        }
        textView.setText(item.name);
        helpPagerImage.setOnClickListener(listener);

        if (item.banner_type.equals(Constants.BundleKeys.VIDEO)) {
            videoThumbImage.setVisibility(View.VISIBLE);
            Picasso.with(helpPagerImage.getContext()).load(list.get(position).image_url).resize(500, 200).centerCrop().
                    placeholder(context.getResources().getDrawable(R.drawable.place_holder_banner)).into(helpPagerImage);
        } else
            Picasso.with(helpPagerImage.getContext()).load(list.get(position).image_url).
                    placeholder(context.getResources().getDrawable(R.drawable.place_holder_banner)).into(helpPagerImage);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}

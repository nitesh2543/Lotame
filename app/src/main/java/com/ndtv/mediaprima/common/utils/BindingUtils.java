package com.ndtv.mediaprima.common.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Date;

/**
 * Created by Elaa on 8/27/2016.
 */
public class BindingUtils {

    @BindingAdapter(value = {"android:src", "default"}, requireAll = true)
    public static void bindImage(ImageView view, String url, Drawable placeHolder) {
        if (TextUtils.isEmpty(url))
            url = null;
        RequestCreator requestCreator =
                Picasso.with(view.getContext()).load(url);
        if (placeHolder != null) {
            requestCreator.placeholder(placeHolder);
        }
        requestCreator.into(view);
    }

    @BindingAdapter({"bind:htmltext"})
    public static void setTitle1(TextView view, String title) {
        if (title != null)
            view.setText(Html.fromHtml(title));
    }

    @BindingAdapter({"bind:releaseDate"})
    public static void setDramaReleaseDate(TextView view, String title) {
        if (title != null)
            view.setText("dikeluarkan " + title);
    }

    @BindingAdapter({"bind:time"})
    public static void timeStandard(TextView textView, String timeSatmp) {
        try {
            Date date = DateUtils.parse(timeSatmp, DateUtils.DRAMA);
            String date1 = DateUtils.getRelativeTime(textView.getContext(), date);
            textView.setText(date1);
        } catch (Exception e) {
            Log.e("TAG", "timeStandard: " + e.toString());
        }
    }

    @BindingAdapter({"bind:minutes"})
    public static void toMinutes(TextView textView, String videoDuration) {
        if (videoDuration != null)
            textView.setText(videoDuration + " min");
    }

}

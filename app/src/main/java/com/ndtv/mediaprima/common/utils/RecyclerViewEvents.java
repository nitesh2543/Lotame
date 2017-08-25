package com.ndtv.mediaprima.common.utils;

import android.view.View;

/**
 * Created by Elaa on 8/29/2016.
 */
public class RecyclerViewEvents {
    /**
     * Callback interface for delivering item clicks.
     */
    public interface Listener<T> {
        /**
         * Called when a item is clicked.
         */
         void onItemClick(T item, View v, int position);
    }


    public static final int ITEM = 0;
    public static final int FOOTER = 1;
    public static final int DRAMA = 2;
    public static final int ARTIST = 3;
    public static final int LIFESTYLE =4;
}

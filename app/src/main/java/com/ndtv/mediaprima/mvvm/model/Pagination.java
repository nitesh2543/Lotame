package com.ndtv.mediaprima.mvvm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ELAA on 18-09-2016.
 */
public class Pagination {
    @SerializedName("total")
    public int total;
    public int count;
    public int per_page;
    public int current_page;
    public int total_pages;

    @SerializedName("links")
    public Links link;


    public class Links{
        @SerializedName("next")
        public String nextLink;
        @SerializedName("previous")
        public String previousLink;
    }
}

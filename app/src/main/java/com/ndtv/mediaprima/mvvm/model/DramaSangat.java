package com.ndtv.mediaprima.mvvm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ELAA on 03-10-2016.
 */
public class DramaSangat {
    @SerializedName("data")
    public List<Data> dataList;
    @SerializedName("banner")
    public List<Banner> bannerList;
}

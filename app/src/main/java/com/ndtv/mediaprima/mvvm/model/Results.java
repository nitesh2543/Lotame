package com.ndtv.mediaprima.mvvm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ELAA on 16-09-2016.
 */
public class Results {

    @SerializedName("data")
    public List<SectionItems> data;
    @SerializedName("meta")
    public Meta meta;
    @SerializedName("categories")
    public List<Categories> categories;
    @SerializedName("banner")
    public List<Banner> banner;


}

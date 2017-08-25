package com.ndtv.mediaprima.mvvm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ELAA on 02-10-2016.
 */
public class Configuration {
    @SerializedName("data")
    public List<ConfigurationItem> list;

    public SearchApi apis;

    public class SearchApi {
        @SerializedName("search_api")
        public String search;
    }
}

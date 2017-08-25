package com.ndtv.mediaprima.mvvm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ELAA on 23-09-2016.
 */
public class SectionItems {

    public int viewType;

    public SectionItems(int viewType) {
        this.viewType = viewType;
    }

    @SerializedName("image_url")
    public String image_url;
    @SerializedName("name")
    public String name;

    @SerializedName("type")
    public String type;
    @SerializedName("uri")
    public String uri;
    @SerializedName("description")
    public String description;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("single_image_url")
    public String fullImage;
    @SerializedName("summary")
    public String summary;
    @SerializedName("video_duration")
    public String video_duration;

    public SectionItems(String name) {
        this.name = name;
    }
}

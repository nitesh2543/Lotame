package com.ndtv.mediaprima.mvvm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ELAA on 18-09-2016.
 */
public class Data {

    public int viewType;

    public Data(int viewType) {
        this.viewType = viewType;
    }

    @SerializedName("image_url")
    public String thumb;
    @SerializedName("name")
    public String name;
    @SerializedName("uri")
    public String uri;
    @SerializedName("icon")
    public String icon;
    @SerializedName("status")
    public String status;
    @SerializedName("group")
    public String group;
    @SerializedName("type")
    public String type;
    @SerializedName("content")
    public String content;

    @SerializedName("section_btn")
    public SectionButton sectionButton;
    @SerializedName("section_items")
    public List<SectionItems> sectionItems;
    @SerializedName("section_type")
    public String sectionType;
    @SerializedName("section_name")
    public String sectionName;
    @SerializedName("category_name")
    public String categoryName;
    @SerializedName("timestamp")
    public String timeStamp;

    @SerializedName("images")
    public List<String> images;

    @SerializedName("articles")
    public Sections articles;
    @SerializedName("gallery")
    public Sections gallery;
    @SerializedName("description")
    public Sections description;
    @SerializedName("dramas")
    public Sections dramas;
    @SerializedName("about")
    public Sections about;

    @SerializedName("artists")
    public Sections artists;
    @SerializedName("trivia")
    public Sections trivia;
    @SerializedName("synopsis")
    public Sections synopsis;
    @SerializedName("created_at")
    public String created_at;
    @SerializedName("embed_code")
    public String embed_code;
    @SerializedName("episodes")
    public Sections episodes;
    @SerializedName("video_preview_image")
    public String video_preview_image;

}

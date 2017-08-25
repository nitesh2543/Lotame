package com.ndtv.mediaprima.mvvm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ELAA on 03-10-2016.
 */
public class Sections {

    @SerializedName("section_name")
    public String section_name;
    @SerializedName("section_content")
    public String section_content;
    @SerializedName("description")
    public String description;
    @SerializedName("section_items")
    public List<SectionItems> section_items;


    @SerializedName("artist_name")
    public String artistName;
    @SerializedName("gender")
    public String gender;
    @SerializedName("nick_name")
    public String nickName;
    @SerializedName("age")
    public String age;
    @SerializedName("date_of_birth")
    public String dateOfBirth;
    @SerializedName("date_of_join")
    public String dateOfJoin;
    @SerializedName("acheivements")
    public String acheivements;
    @SerializedName("home_town")
    public String homeTown;

    public List<ArtistProfile> artist_profile;

}

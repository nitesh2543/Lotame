package com.ndtv.mediaprima.mvvm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nitesh on 1/23/2017.
 */

public class ConfigurationItem {

    public ConfigurationItem(String name, String uri, String group, String type, String icon) {
        this.name = name;
        this.uri = uri;
        this.group = group;
        this.type = type;
        this.icon = icon;
    }

    @SerializedName("name")
    public String name;
    @SerializedName("uri")
    public String uri;
    @SerializedName("status")
    public String status;
    @SerializedName("group")
    public String group;
    @SerializedName("type")
    public String type;
    @SerializedName("icon")
    public String icon;
}

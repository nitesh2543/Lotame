package com.ndtv.mediaprima.mvvm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ELAA on 23-09-2016.
 */
public class Result implements Parcelable{

    @SerializedName("data")
    public Data data;
    @SerializedName("banner")
    public List<Banner> bannerList;

    protected Result(Parcel in) {
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}

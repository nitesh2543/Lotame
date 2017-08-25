package com.ndtv.mediaprima.mvvm.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nitesh on 2/3/2017.
 */

public class Countries {

    @SerializedName("geonames")
    public List<Country> countryList;

    public class Country {

        public String continent;
        public String capital;
        public String languages;
        public String geonameId;
        public String south;
        public String isoAlpha3;
        public String north;
        public String fipsCode;
        public String population;
        public String east;
        public String isoNumeric;
        public String areaInSqKm;
        public String countryCode;
        public String west;
        public String countryName;
        public String continentName;
        public String currencyCode;

    }
}

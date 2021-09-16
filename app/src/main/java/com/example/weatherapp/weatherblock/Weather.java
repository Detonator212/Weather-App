package com.example.weatherapp.weatherblock;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("main")
    private String weatherType;
    private String description;
    private String icon;

    public String getMain() {
        return weatherType;
    }

    public String getDescription() {
        return description.substring(0,1).toUpperCase() + description.substring(1);
    }

    public String getIcon() {
        return icon;
    }
}

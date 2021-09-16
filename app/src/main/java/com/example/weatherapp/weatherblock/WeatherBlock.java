package com.example.weatherapp.weatherblock;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherBlock {
    private Main main;
    private ArrayList<Weather> weather;
    @SerializedName("dt_txt")
    private String time;

    public Main getMain() {
        return main;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public String getTime() {
        return time;
    }
}

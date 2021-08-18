package com.example.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherBlockRoot {
    private ArrayList<WeatherBlock> list;

    public ArrayList<WeatherBlock> getWeatherBlocks() {
        return list;
    }
}

class WeatherBlock {
    private Main main;
    private ArrayList<Weather> weather;

    public Main getMain() {
        return main;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }
}

class Main {
    private float temp;
    private float feels_like;
    private int humidity;

    public float getTemp() {
        return temp;
    }

    public float getFeels_like() {
        return feels_like;
    }

    public float getHumidity() {
        return humidity;
    }
}

class Weather {
    @SerializedName("main")
    private String weatherType;
    @SerializedName("description")
    private String weatherDescription;

    public String getMain() {
        return weatherType;
    }

    public String getDescription() {
        return weatherDescription;
    }
}
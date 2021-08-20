package com.example.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WeatherBlockRoot {
    private ArrayList<WeatherBlock> list;
    private City city;

    public ArrayList<WeatherBlock> getWeatherBlocks() {
        return list;
    }

    public City getCity() {
        return city;
    }
}

class WeatherBlock {
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

class Main {
    private float temp;
    private float feels_like;
    private int humidity;

    public String getTemp() {
        return String.valueOf(Math.round(temp - 273.15)) + "°C";
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

class City {
    String name;

    public String getName() {
        return name;
    }
}
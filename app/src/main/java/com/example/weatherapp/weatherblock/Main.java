package com.example.weatherapp.weatherblock;

public class Main {
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
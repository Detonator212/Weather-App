package com.example.weatherapp.interfaces;

import com.example.weatherapp.weatherblock.WeatherBlockRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    String apiKey = "4c9acb76930dd424c11f21602b0e0b2d";

    @GET("data/2.5/forecast")
    Call<WeatherBlockRoot> getWeather(
            @Query("q") String city,
            @Query("appid") String apiKey
    );
}

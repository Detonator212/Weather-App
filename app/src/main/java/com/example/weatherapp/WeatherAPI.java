package com.example.weatherapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WeatherAPI {

    @GET("data/2.5/forecast?q=london&appid=4c9acb76930dd424c11f21602b0e0b2d")
    Call<WeatherBlockRoot> getWeather();
}

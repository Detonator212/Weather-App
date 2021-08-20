package com.example.weatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.weatherapp.databinding.FragmentFirstBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherAPI weatherAPI = retrofit.create(WeatherAPI.class);

        Call<WeatherBlockRoot> call = weatherAPI.getWeather();

        call.enqueue(new Callback<WeatherBlockRoot>() {
            @Override
            public void onResponse(Call<WeatherBlockRoot> call, Response<WeatherBlockRoot> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                WeatherBlockRoot weatherBlockRoot = (WeatherBlockRoot) response.body();
                List<WeatherBlock> weatherBlocks = weatherBlockRoot.getWeatherBlocks();

                binding.currentCity.setText(weatherBlockRoot.getCity().getName());
                binding.currentTemp.setText(weatherBlocks.get(0).getMain().getTemp());
                binding.currentWeatherDescription.setText(weatherBlocks.get(0).getWeather().get(0).getDescription());

                for (WeatherBlock weatherBlock : weatherBlocks) {
                    WeatherBlockUI newWeatherBlockUI = new WeatherBlockUI(getContext(), null);

                    newWeatherBlockUI.setTemperature(weatherBlock.getMain().getTemp());
                    newWeatherBlockUI.setIcon(weatherBlock.getWeather().get(0).getIcon());
                    newWeatherBlockUI.setDescription(weatherBlock.getWeather().get(0).getDescription());
                    newWeatherBlockUI.setDate(weatherBlock.getTime().substring(0, 10));
                    newWeatherBlockUI.setTime(weatherBlock.getTime().substring(11, 16));

                    LinearLayout linearLayout = binding.weatherBlocksLinearLayout;
                    linearLayout.addView(newWeatherBlockUI);
                }

            }

            @Override
            public void onFailure(Call<WeatherBlockRoot> call, Throwable t) {
                System.out.println("API request failed");
            }
        });

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
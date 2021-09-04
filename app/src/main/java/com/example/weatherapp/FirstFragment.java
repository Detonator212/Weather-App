package com.example.weatherapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherapp.databinding.FragmentFirstBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private String cityName;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    List<String> cities;
    private int numRefreshedLocations = 0;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        cityName = ((MainActivity) requireActivity()).cityName;
        swipeRefreshLayout = binding.swipeRefreshLayout;

        FileAccessor fileAccessor = new FileAccessor(getContext());

        if (!(citiesFileExists())) {
            fileAccessor.createDefaultFile("London");
        }

        cities = fileAccessor.readFile();
        linearLayout = binding.linearLayout;

        swipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    numRefreshedLocations = 0;
                    refresh();
                }
        });

        refresh();

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

    public void refresh() {

        List<WeatherBlocksContainer> weatherBlocksContainers = new ArrayList<>();

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof WeatherBlocksContainer) {
                weatherBlocksContainers.add((WeatherBlocksContainer) linearLayout.getChildAt(i));
            }
        }

        for (WeatherBlocksContainer weatherBlocksContainer : weatherBlocksContainers) {
            linearLayout.removeView(weatherBlocksContainer);
        }

        for (String city : cities) {
            WeatherBlocksContainer newWeatherBlocksContainer = new WeatherBlocksContainer(getContext());
            newWeatherBlocksContainer.setLocationTitle(city);
            if (city.equals(cities.get(0))){
                newWeatherBlocksContainer.locationTitle.setVisibility(getView().GONE); }
            linearLayout.addView(newWeatherBlocksContainer);
            fetchWeatherData(city, newWeatherBlocksContainer);
        }
    }

    public void checkIfRefreshComplete() {
        if (numRefreshedLocations == cities.size()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void fetchWeatherData(String location, WeatherBlocksContainer weatherBlocksContainer) {
        WeatherAPI weatherAPI = WeatherApiGenerator.createService(WeatherAPI.class);

        Call<WeatherBlockRoot> call = weatherAPI.getWeather(location, WeatherAPI.apiKey);
        call.enqueue(new Callback<WeatherBlockRoot>() {
            @Override
            public void onResponse(Call<WeatherBlockRoot> call, Response<WeatherBlockRoot> response) {
                if (!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                WeatherBlockRoot weatherBlockRoot = response.body();
                List<WeatherBlock> weatherBlocks = weatherBlockRoot.getWeatherBlocks();

                // If this is the first city in the user's saved cities then set the text at top of screen
                if (location.equals(cities.get(0))) {
                    binding.currentCity.setText(weatherBlockRoot.getCity().getName());
                    binding.currentTemp.setText(weatherBlocks.get(0).getMain().getTemp());
                    binding.currentWeatherDescription.setText(weatherBlocks.get(0).getWeather().get(0).getDescription());
                }


                for (WeatherBlock weatherBlock : weatherBlocks) {
                    WeatherBlockUI newWeatherBlockUI = new WeatherBlockUI(getContext(), null);

                    newWeatherBlockUI.setTemperature(weatherBlock.getMain().getTemp());
                    newWeatherBlockUI.setIcon(weatherBlock.getWeather().get(0).getIcon());
                    newWeatherBlockUI.setDescription(weatherBlock.getWeather().get(0).getDescription());
                    newWeatherBlockUI.setDate(weatherBlock.getTime().substring(0, 10));
                    newWeatherBlockUI.setTime(weatherBlock.getTime().substring(11, 16));

                    LinearLayout linearLayout = weatherBlocksContainer.linearLayout;
                    linearLayout.addView(newWeatherBlockUI);
                }

                numRefreshedLocations++;
                checkIfRefreshComplete();
            }

            @Override
            public void onFailure(Call<WeatherBlockRoot> call, Throwable t) {
                System.out.println("API request failed");
                numRefreshedLocations++;
                checkIfRefreshComplete();
            }
        });
    }

    public boolean citiesFileExists() {
        File dirFiles = getContext().getFilesDir();
        File[] filesArray = dirFiles.listFiles();
        for (File file : filesArray) {
            if (file.getName().equals("Cities.txt")) {
                return true;
            }
        }
        return false;
    }
}
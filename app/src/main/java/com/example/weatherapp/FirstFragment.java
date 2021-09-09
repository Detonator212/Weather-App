package com.example.weatherapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherapp.databinding.FragmentFirstBinding;

import java.io.File;
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
    Boolean lightCardTheme = true;

    MainActivity activity;

    List<WeatherBlocksContainer> weatherBlocksContainers = new ArrayList<>();
    List<WeatherBlockUI> weatherBlockUIS = new ArrayList<>();

    public FirstFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        binding = FragmentFirstBinding.inflate(inflater, container, false);

        linearLayout = binding.linearLayout;

        cityName = ((MainActivity) requireActivity()).cityName;
        swipeRefreshLayout = binding.swipeRefreshLayout;

        FileAccessor fileAccessor = new FileAccessor(getContext());

        if (!(citiesFileExists())) {
            fileAccessor.createDefaultFile("London");
        }

        cities = fileAccessor.readFile();

        swipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    numRefreshedLocations = 0;
                    refresh();
                }
        });

        for (String city : cities) {
            WeatherBlocksContainer newWeatherBlocksContainer = new WeatherBlocksContainer(getContext());
            newWeatherBlocksContainer.setLocationTitle(city);
            if (city.equals(cities.get(0))){
                newWeatherBlocksContainer.locationTitle.setVisibility(getView().GONE); }
            linearLayout.addView(newWeatherBlocksContainer);
            weatherBlocksContainers.add(newWeatherBlocksContainer);
            fetchWeatherData(city, newWeatherBlocksContainer);
        }

        return binding.getRoot();
    }

    public void moveAboveNavBar(View v, WindowInsetsCompat windowInsets) {
        Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
        linearLayout.setPadding(0, 0, 0,insets.bottom);
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
        weatherBlockUIS = new ArrayList<>();
        int count = 0;
        for (WeatherBlocksContainer weatherBlocksContainer : weatherBlocksContainers) {
            weatherBlocksContainer.addLoadingIcon();
            weatherBlocksContainer.clearContents();
            fetchWeatherData(cities.get(count), weatherBlocksContainer);
            count++;
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
                    setTheme(weatherBlockRoot.getWeatherBlocks().get(0).getWeather().get(0).getIcon());
                }


                for (WeatherBlock weatherBlock : weatherBlocks) {
                    WeatherBlockUI newWeatherBlockUI = new WeatherBlockUI(getContext(), null);

                    newWeatherBlockUI.setTemperature(weatherBlock.getMain().getTemp());
                    newWeatherBlockUI.setIcon(weatherBlock.getWeather().get(0).getIcon());
                    newWeatherBlockUI.setDescription(weatherBlock.getWeather().get(0).getDescription());
                    newWeatherBlockUI.setDate(weatherBlock.getTime().substring(0, 10));
                    newWeatherBlockUI.setTime(weatherBlock.getTime().substring(11, 16));

                    weatherBlockUIS.add(newWeatherBlockUI);
                    newWeatherBlockUI.setTheme(lightCardTheme);

                    LinearLayout linearLayout = weatherBlocksContainer.linearLayout;
                    linearLayout.addView(newWeatherBlockUI);
                }

                weatherBlocksContainer.removeLoadingIcon();
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

    public void setTheme(String icon) {
        icon = icon.substring(0,2);
        String background;
        switch (icon) {
            case "01":
                background = "clear";
                lightCardTheme = true;
                break;
            case "02":
                background = "fewclouds";
                lightCardTheme = true;
                break;
            case "03":
                background = "scatteredclouds";
                lightCardTheme = false;
                break;
            case "04":
                background = "brokenclouds";
                lightCardTheme = false;
                break;
            case "09":
                background = "showerrain";
                lightCardTheme = false;
                break;
            case "10":
                background = "rain";
                lightCardTheme = false;
                break;
            case "11":
                background = "thunderstorm";
                lightCardTheme = false;
                break;
            case "13":
                background = "snow";
                lightCardTheme = true;
                break;
            case "50":
                background = "mist";
                lightCardTheme = true;
                break;
            default:
                lightCardTheme = false;
                throw new IllegalStateException("Unexpected value: " + icon);
        }
        // TODO add night backgrounds
        activity.backgroundImage.setImageResource(getResources().getIdentifier(background + "_day", "drawable", getContext().getPackageName()));

        for (WeatherBlocksContainer weatherBlocksContainer : weatherBlocksContainers) {
            weatherBlocksContainer.setCardColor(lightCardTheme);
        }
        for (WeatherBlockUI weatherBlockUI : weatherBlockUIS) {
            weatherBlockUI.setTheme(lightCardTheme);
        }
    }
}
package com.example.weatherapp.fragments;

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

import com.example.weatherapp.FileAccessor;
import com.example.weatherapp.interfaces.WeatherAPI;
import com.example.weatherapp.WeatherApiGenerator;
import com.example.weatherapp.weatherblock.WeatherBlock;
import com.example.weatherapp.weatherblock.WeatherBlockRoot;
import com.example.weatherapp.activities.MainActivity;
import com.example.weatherapp.views.WeatherBlockView;
import com.example.weatherapp.views.WeatherBlocksContainerView;
import com.example.weatherapp.databinding.FragmentWeatherListBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragment for displaying weather data
 */
public class WeatherListFragment extends Fragment {

    private FragmentWeatherListBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    private List<String> cities;
    private int numRefreshedLocations = 0;
    private Boolean lightCardTheme = true;

    /**
     * The activity this fragment is in
     */
    private MainActivity activity;

    private List<WeatherBlocksContainerView> weatherBlocksContainerViews = new ArrayList<>();
    private List<WeatherBlockView> weatherBlockViews = new ArrayList<>();

    /**
     * Constructor to define the activity this fragment is in
     * @param activity The activity this fragment is in
     */
    public WeatherListFragment(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWeatherListBinding.inflate(inflater, container, false);

        linearLayout = binding.linearLayout;
        swipeRefreshLayout = binding.swipeRefreshLayout;

        FileAccessor fileAccessor = new FileAccessor(getContext());
        // If no locations save file exists create save file containing London
        if (!(citiesFileExists())) {
            fileAccessor.createDefaultFile("London");
        }
        cities = fileAccessor.readFile();

        // Swipe to refresh functionality
        swipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    numRefreshedLocations = 0;
                    refresh();
                }
        });

        // Initial fetching of all weather data and displaying on UI
        for (String city : cities) {
            WeatherBlocksContainerView newWeatherBlocksContainerView = new WeatherBlocksContainerView(getContext());
            newWeatherBlocksContainerView.setLocationTitle(city);
            if (city.equals(cities.get(0))){
                newWeatherBlocksContainerView.getLocationTitle().setVisibility(getView().GONE); }
            linearLayout.addView(newWeatherBlocksContainerView);
            weatherBlocksContainerViews.add(newWeatherBlocksContainerView);
            fetchWeatherData(city, newWeatherBlocksContainerView);
        }

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

    /**
     * Move contents above navbar
     * @param windowInsets System inset sizes
     */
    public void moveAboveNavBar(WindowInsetsCompat windowInsets) {
        Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
        linearLayout.setPadding(0, 0, 0,insets.bottom);
    }

    /**
     * Refresh list of weather data
     */
    private void refresh() {
        weatherBlockViews = new ArrayList<>();
        int count = 0;
        for (WeatherBlocksContainerView weatherBlocksContainerView : weatherBlocksContainerViews) {
            weatherBlocksContainerView.addLoadingIcon();
            weatherBlocksContainerView.clearContents();
            fetchWeatherData(cities.get(count), weatherBlocksContainerView);
            count++;
        }
    }

    /**
     * Check if all API requests are flagged as completed
     */
    private void checkIfRefreshComplete() {
        if (numRefreshedLocations == cities.size()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Fetch weather data from API and display in UI
     * @param location Name of location to fetch weather data for
     * @param weatherBlocksContainerView Which card view to add fetched data to
     */
    private void fetchWeatherData(String location, WeatherBlocksContainerView weatherBlocksContainerView) {
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

                // Add each of block of weather data to card view
                for (WeatherBlock weatherBlock : weatherBlocks) {
                    WeatherBlockView newWeatherBlockView = new WeatherBlockView(getContext(), null);

                    newWeatherBlockView.setTemperature(weatherBlock.getMain().getTemp());
                    newWeatherBlockView.setIcon(weatherBlock.getWeather().get(0).getIcon());
                    newWeatherBlockView.setDescription(weatherBlock.getWeather().get(0).getDescription());
                    newWeatherBlockView.setDate(weatherBlock.getTime().substring(0, 10));
                    newWeatherBlockView.setTime(weatherBlock.getTime().substring(11, 16));

                    weatherBlockViews.add(newWeatherBlockView);
                    newWeatherBlockView.setTheme(lightCardTheme);

                    LinearLayout linearLayout = weatherBlocksContainerView.getLinearLayout();
                    linearLayout.addView(newWeatherBlockView);
                }

                weatherBlocksContainerView.removeLoadingIcon();
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

    /**
     * @return Whether a file containing saved places exists
     */
    private boolean citiesFileExists() {
        File dirFiles = getContext().getFilesDir();
        File[] filesArray = dirFiles.listFiles();
        for (File file : filesArray) {
            if (file.getName().equals("Cities.txt")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set light or dark theme depending on weather conditions
     * @param icon OpenWeather assigned icon to these weather conditions
     */
    private void setTheme(String icon) {
        String weatherIcon = icon.substring(0,2);

        String dayOrNight;
        if (icon.charAt(2) == 'd') {
            dayOrNight = "_day";
        } else {
            dayOrNight = "_night";
        }

        String background;
        switch (weatherIcon) {
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

        activity.setBackgroundImage(background + dayOrNight);

        for (WeatherBlocksContainerView weatherBlocksContainerView : weatherBlocksContainerViews) {
            weatherBlocksContainerView.setCardColor(lightCardTheme);
        }
        for (WeatherBlockView weatherBlockView : weatherBlockViews) {
            weatherBlockView.setTheme(lightCardTheme);
        }
    }
}
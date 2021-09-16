package com.example.weatherapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.weatherapp.adapters.CitiesRecViewAdapter;
import com.example.weatherapp.FileAccessor;
import com.example.weatherapp.ListCity;
import com.example.weatherapp.interfaces.OnCityClickListener;
import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ActivitySearchBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Activity for searching for a new location
 */
public class SearchActivity extends AppCompatActivity implements OnCityClickListener {

    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup recycler view of cities
        ArrayList<ListCity> listCities = jsonStringToList(readCitiesJson());
        CitiesRecViewAdapter adapter = new CitiesRecViewAdapter(this);
        adapter.setListCities(listCities);
        RecyclerView citiesRecView = binding.citiesList;
        citiesRecView.setAdapter(adapter);
        citiesRecView.setLayoutManager(new LinearLayoutManager(this));

        // Search bar functionality
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(java.lang.String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(java.lang.String s) {
                adapter.filter(s);
                return true;
            }
        });

        // Top corner back button functionality
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                finish();
            }
        });
    }

    private void hideKeyboard() {
        Activity activity = this;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Read the world-cities file
     * @return A string of all the cities in the world
     */
    private String readCitiesJson() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("world-cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new java.lang.String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Convert string of json file to arraylist
     * @param jsonString String of json file
     * @return ArrayList of ListCity objects
     */
    private ArrayList<ListCity> jsonStringToList(String jsonString) {
        ArrayList<ListCity> listCities = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentObj = jsonArray.getJSONObject(i);
                listCities.add(new ListCity(currentObj.getString("name"), currentObj.getString("country")));
            }
            return listCities;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onCityClick(String city) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("selected_city", city);
        FileAccessor fileAccessor = new FileAccessor(this);
        fileAccessor.saveCity(city);
        startActivity(intent);
    }
}
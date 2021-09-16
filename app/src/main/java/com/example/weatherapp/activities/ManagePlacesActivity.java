package com.example.weatherapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.weatherapp.FileAccessor;
import com.example.weatherapp.ItemTouchHelperCallback;
import com.example.weatherapp.adapters.ManagePlacesRecViewAdapter;
import com.example.weatherapp.R;
import com.example.weatherapp.databinding.ActivityManagePlacesBinding;

import java.util.ArrayList;

/**
 * Activity for managing saved locations
 */
public class ManagePlacesActivity extends AppCompatActivity {

    private ActivityManagePlacesBinding binding;
    private RecyclerView recyclerView;
    private ArrayList<String> places;
    private final ArrayList<String> placesCopy = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityManagePlacesBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        FileAccessor fileAccessor = new FileAccessor(this);
        places = (ArrayList<String>) fileAccessor.readFile();
        placesCopy.addAll(places);

        ManagePlacesRecViewAdapter adapter = new ManagePlacesRecViewAdapter();
        adapter.setPlaces(places);

        recyclerView = binding.placesList;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Top corner back button functionality
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Save changes to saved places file and go back to main activity
     */
    public void goBack() {
        if (places.equals(placesCopy)) {
            finish();
            return;
        }
        FileAccessor fileAccessor = new FileAccessor(this);
        fileAccessor.overwriteFile(places);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Call goBack() when system back button is pressed
     */
    @Override
    public void onBackPressed() {
        goBack();
    }
}
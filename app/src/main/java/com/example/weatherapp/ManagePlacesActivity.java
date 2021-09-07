package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ManagePlacesActivity extends AppCompatActivity {

    private RecyclerView citiesRecView;
    private ArrayList<String> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_places);

        FileAccessor fileAccessor = new FileAccessor(this);
        places = (ArrayList<String>) fileAccessor.readFile();

        ManagePlacesRecViewAdapter adapter = new ManagePlacesRecViewAdapter();
        adapter.setPlaces(places);

        citiesRecView = findViewById(R.id.places_list);
        citiesRecView.setAdapter(adapter);
        citiesRecView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }

    public void goBack() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
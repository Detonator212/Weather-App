package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ManagePlacesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_places);

        FileAccessor fileAccessor = new FileAccessor(this);
        places = (ArrayList<String>) fileAccessor.readFile();

        ManagePlacesRecViewAdapter adapter = new ManagePlacesRecViewAdapter();
        adapter.setPlaces(places);

        recyclerView = findViewById(R.id.places_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

    }

    public void goBack() {
        FileAccessor fileAccessor = new FileAccessor(this);
        fileAccessor.overwriteFile(places);
        ((ManagePlacesRecViewAdapter) recyclerView.getAdapter()).getPlaces();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}
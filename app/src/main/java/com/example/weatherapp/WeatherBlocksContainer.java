package com.example.weatherapp;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class WeatherBlocksContainer extends LinearLayout {

    TextView locationTitle;
    CardView cardView;
    LinearLayout linearLayout;

    public WeatherBlocksContainer(@NonNull Context context) {
        super(context);

        inflate(context, R.layout.weather_blocks_container, this);

        locationTitle = findViewById(R.id.location_title);
        cardView = findViewById(R.id.card_view);
        linearLayout = findViewById(R.id.linear_layout);
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle.setText(locationTitle);
    }
}

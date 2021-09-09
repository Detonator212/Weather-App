package com.example.weatherapp;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

public class WeatherBlocksContainer extends LinearLayout {

    TextView locationTitle;
    CardView cardView;
    LinearLayout linearLayout;
    ProgressBar progressBar;

    public WeatherBlocksContainer(@NonNull Context context) {
        super(context);

        inflate(context, R.layout.weather_blocks_container, this);

        locationTitle = findViewById(R.id.location_title);
        cardView = findViewById(R.id.card_view);
        linearLayout = findViewById(R.id.linear_layout);
        progressBar = findViewById(R.id.progressBar);
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle.setText(locationTitle);
    }

    public void removeLocationTitle() {
        this.removeView(locationTitle);
    }

    public void clearContents() { linearLayout.removeAllViews(); }

    public void removeLoadingIcon() { cardView.removeView(progressBar); }

    public void addLoadingIcon() { cardView.addView(progressBar); }

    public void setCardColor(Boolean lightCard) {
        if (lightCard) {
            cardView.getBackground().setTint(Color.WHITE);
        } else {
            cardView.getBackground().setTint(Color.DKGRAY);
        }
    }
}

package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class WeatherBlockUI extends LinearLayout {

    public WeatherBlockUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.weather_block_ui, this);

        TextView temperature = findViewById(R.id.temperature);
        TextView description = findViewById(R.id.description);
        TextView time = findViewById(R.id.time);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.WeatherBlockUI);
        temperature.setText(attributes.getString(R.styleable.WeatherBlockUI_temperature));
        description.setText(attributes.getString(R.styleable.WeatherBlockUI_description));
        time.setText(attributes.getString(R.styleable.WeatherBlockUI_time));
    }
}
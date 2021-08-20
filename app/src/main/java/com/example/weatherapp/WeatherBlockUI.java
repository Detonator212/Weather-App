package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class WeatherBlockUI extends LinearLayout {

    TextView temperature;
    ImageView image;
    TextView description;
    TextView time;

    public WeatherBlockUI(Context context, @Nullable AttributeSet attrs) {
        super(context);

        inflate(context, R.layout.weather_block_ui, this);

        temperature = findViewById(R.id.temperature);
        image = findViewById(R.id.image);
        description = findViewById(R.id.description);
        time = findViewById(R.id.time);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.WeatherBlockUI);
        temperature.setText(attributes.getString(R.styleable.WeatherBlockUI_temperature));
        description.setText(attributes.getString(R.styleable.WeatherBlockUI_description));
        time.setText(attributes.getString(R.styleable.WeatherBlockUI_time));
        attributes.recycle();
    }

    public void setTemperature(String temperature) {
        this.temperature.setText(temperature);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public void setTime(String time) {
        this.time.setText(time);
    }

    public void setIcon(String icon) {
        Glide.with(this).load("https://openweathermap.org/img/wn/" + icon + "@2x.png").into(image);
    }
}
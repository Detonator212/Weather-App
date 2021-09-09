package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

public class WeatherBlockUI extends LinearLayout {

    TextView temperature;
    ImageView image;
    TextView description;
    TextView day;
    TextView time;

    public WeatherBlockUI(Context context, @Nullable AttributeSet attrs) {
        super(context);

        inflate(context, R.layout.weather_block_ui, this);

        temperature = findViewById(R.id.temperature);
        image = findViewById(R.id.image);
        description = findViewById(R.id.description);
        day = findViewById(R.id.day);
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

    public void setDate(String date) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format1.parse(date);
            DateFormat format2 = new SimpleDateFormat("EEEE");
            String finalDay = format2.format(date1);
            this.day.setText(finalDay.substring(0,3));
        } catch (Exception e) {
            this.day.setText("n/a");
        }
    }

    public void setIcon(String icon) {
        Glide.with(this).load("https://openweathermap.org/img/wn/" + icon + "@2x.png").into(image);
    }

    public void setTheme(Boolean light) {
        if (light) {
            temperature.setTextColor(Color.BLACK);
            description.setTextColor(Color.BLACK);
            day.setTextColor(Color.BLACK);
            time.setTextColor(Color.BLACK);
        } else {
            temperature.setTextColor(Color.WHITE);
            description.setTextColor(Color.WHITE);
            day.setTextColor(Color.WHITE);
            time.setTextColor(Color.WHITE);
        }
    }
}
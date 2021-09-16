package com.example.weatherapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.weatherapp.R;
import com.example.weatherapp.fragments.WeatherListFragment;
import com.example.weatherapp.databinding.ActivityMainBinding;

import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

/**
 * The Main Activity used to display the weather list and allow navigation to other parts of the app
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private Intent intent;
    private String cityName;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make activity render behind system bars
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        intent = getIntent();
        cityName = intent.getStringExtra("selected_city");
        backgroundImage = binding.backgroundImage;

        // Create weather list fragment and add to activity UI
        WeatherListFragment weatherListFragment = new WeatherListFragment(this);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, weatherListFragment, null)
                .commit();

        // Move app out from underneath system bars
        Toolbar toolbar = binding.toolbar;
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.topMargin = insets.top;
            v.setLayoutParams(mlp);
            weatherListFragment.moveAboveNavBar(windowInsets);
            return WindowInsetsCompat.CONSUMED;
        });

        // Search button functionality
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        // Popup menu functionality
        binding.popupMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, binding.popupMenuButton);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.settings:
                                // TODO create settings menu
                            case R.id.manage_places:
                                Intent intent = new Intent(view.getContext(), ManagePlacesActivity.class);
                                startActivity(intent);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    public void setBackgroundImage(String name) {
        backgroundImage.setImageResource(getResources().getIdentifier(name, "drawable", getPackageName()));
    }
}
package com.example.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.ListCity;
import com.example.weatherapp.interfaces.OnCityClickListener;
import com.example.weatherapp.R;

import java.util.ArrayList;

public class CitiesRecViewAdapter extends RecyclerView.Adapter<CitiesRecViewAdapter.ViewHolder> {

    private ArrayList<ListCity> listCities = new ArrayList<>();
    private ArrayList<ListCity> listCitiesCopy = new ArrayList<>();

    private OnCityClickListener onCityClickListener;

    public void setListCities(ArrayList<ListCity> listCities) {
        this.listCities = listCities;
        this.listCitiesCopy.addAll(listCities);
    }

    public CitiesRecViewAdapter(OnCityClickListener onCityClickListener) {
        this.onCityClickListener = onCityClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view, onCityClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cityName.setText(listCities.get(position).getName());
        holder.countryName.setText(listCities.get(position).getCountry());
    }

    @Override
    public int getItemCount() {
        return listCities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView cityName;
        private TextView countryName;

        OnCityClickListener onCityClickListener;

        public ViewHolder(@NonNull View itemView, OnCityClickListener onCityClickListener) {
            super(itemView);
            cityName = itemView.findViewById(R.id.city_name);
            countryName = itemView.findViewById(R.id.country_name);
            this.onCityClickListener = onCityClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCityClickListener.onCityClick(cityName.getText().toString());
        }
    }

    public void filter(java.lang.String text) {
        listCities.clear();
        if(text.isEmpty()) {
            listCities.addAll(listCitiesCopy);
        } else {
            text = text.toLowerCase();
            for (ListCity listCity : listCitiesCopy) {
                if (listCity.getName().toLowerCase().contains(text) || listCity.getCountry().toLowerCase().contains(text)) {
                    listCities.add(listCity);
                }
            }
        }
        notifyDataSetChanged();
    }
}

package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CitiesRecViewAdapter extends RecyclerView.Adapter<CitiesRecViewAdapter.ViewHolder> {

    private ArrayList<ListCity> listCities = new ArrayList<>();
    private ArrayList<ListCity> listCitiesCopy = new ArrayList<>();

    public void setListCities(ArrayList<ListCity> listCities) {
        this.listCities = listCities;
        this.listCitiesCopy.addAll(listCities);
    }

    public CitiesRecViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cityName;
        private TextView countryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.city_name);
            countryName = itemView.findViewById(R.id.country_name);
        }
    }

    public void filter(String text) {
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

        System.out.println(listCities.size());
        System.out.println(listCitiesCopy.size());
        notifyDataSetChanged();
    }
}

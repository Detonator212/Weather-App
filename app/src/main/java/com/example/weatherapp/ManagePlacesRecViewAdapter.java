package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ManagePlacesRecViewAdapter extends RecyclerView.Adapter<ManagePlacesRecViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private List<String> places = new ArrayList<>();

    public void setPlaces(ArrayList<String> places) {
        this.places = places;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_places_item, parent, false);
        ManagePlacesRecViewAdapter.ViewHolder holder = new ManagePlacesRecViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.placeName.setText(places.get(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(places, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(places, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        places.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView placeName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            placeName = itemView.findViewById(R.id.place_name);
        }
    }

    public List<String> getPlaces() {
        return places;
    }
}

package com.example.airportapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airportapp.R;
import com.example.airportapp.model.Flight;

import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.ViewHolder> {
    private List<Flight> flights;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Flight flight);
    }

    public FlightAdapter(List<Flight> flights, OnItemClickListener listener) {
        this.flights = flights;
        this.listener = listener;
    }

    //Création visuelle de l’item à partir du fichier XML item_flight.xml

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flight, parent, false);
        return new ViewHolder(view);
    }

    //Récupère le vol de la position actuelle
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flight flight = flights.get(position);
        holder.bind(flight, listener);
    }

    //nombre tot de l items dans la list
    @Override
    public int getItemCount() {
        return flights.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView routeTv;

        //recuperation ds TextView
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            routeTv = itemView.findViewById(R.id.routeTv);
        }

        public void bind(Flight flight, OnItemClickListener listener) {
            nameTv.setText(flight.getName());
            routeTv.setText(flight.getOrigin() + " → " + flight.getDestination());

            itemView.setOnClickListener(v -> listener.onItemClick(flight));
        }
    }
}
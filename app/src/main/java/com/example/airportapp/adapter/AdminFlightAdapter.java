package com.example.airportapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airportapp.R;
import com.example.airportapp.model.Flight;

import java.util.List;

public class AdminFlightAdapter extends RecyclerView.Adapter<AdminFlightAdapter.ViewHolder> {
    private List<Flight> flights;
    private OnFlightActionListener listener;

    public interface OnFlightActionListener {
        void onEditFlight(Flight flight);
        void onDeleteFlight(Flight flight);
    }

    public AdminFlightAdapter(List<Flight> flights, OnFlightActionListener listener) {
        this.flights = flights;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_flight, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flight flight = flights.get(position);
        holder.bind(flight, listener);
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView routeTv;
        TextView idTv;
        Button editBtn;
        Button deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            routeTv = itemView.findViewById(R.id.routeTv);
            idTv = itemView.findViewById(R.id.idTv);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }

        public void bind(Flight flight, OnFlightActionListener listener) {
            nameTv.setText(flight.getName());
            routeTv.setText(flight.getOrigin() + " → " + flight.getDestination());
            idTv.setText("ID: " + flight.getId());

            editBtn.setOnClickListener(v -> listener.onEditFlight(flight));
            deleteBtn.setOnClickListener(v -> listener.onDeleteFlight(flight));
        }
    }
}
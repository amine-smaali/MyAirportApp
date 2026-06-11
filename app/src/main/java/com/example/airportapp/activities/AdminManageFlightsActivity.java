package com.example.airportapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airportapp.R;
import com.example.airportapp.adapter.AdminFlightAdapter;
import com.example.airportapp.db.DatabaseHelper;
import com.example.airportapp.model.Flight;

import java.util.List;

public class AdminManageFlightsActivity extends AppCompatActivity implements AdminFlightAdapter.OnFlightActionListener {
    private RecyclerView recyclerView;
    private AdminFlightAdapter adapter;
    private DatabaseHelper db;
    private List<Flight> flights;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_flights);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFlights();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminManageFlightsActivity.this , AdminDashboardActivity.class);
            startActivity(intent);
        });
    }

    private void loadFlights() {
        flights = db.getAllFlights();
        adapter = new AdminFlightAdapter(flights, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEditFlight(Flight flight) {
        Intent intent = new Intent(this, AdminEditFlightActivity.class);
        intent.putExtra("FLIGHT_ID", flight.getId());
        intent.putExtra("FLIGHT_NAME", flight.getName());
        intent.putExtra("FLIGHT_ORIGIN", flight.getOrigin());
        intent.putExtra("FLIGHT_DESTINATION", flight.getDestination());
        startActivity(intent);
    }

    @Override
    public void onDeleteFlight(Flight flight) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Flight")
                .setMessage("Are you sure you want to delete flight " + flight.getName() + "?\nThis will also delete all reservations for this flight.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean success = db.deleteFlight(flight.getId());
                    if (success) {
                        Toast.makeText(this, "Flight deleted successfully", Toast.LENGTH_SHORT).show();
                        loadFlights(); // Refresh list
                    } else {
                        Toast.makeText(this, "Failed to delete flight", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFlights(); // Refresh when returning to activity
    }
}
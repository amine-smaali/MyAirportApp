package com.example.airportapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airportapp.R;
import com.example.airportapp.db.DatabaseHelper;
import com.example.airportapp.model.Flight;

public class FlightDetailActivity extends AppCompatActivity {
    private TextView nameTv;
    private TextView originTv;
    private TextView destinationTv;
    private Button reserveBtn;
    private Button viewAllReservationsBtn; // Nouveau bouton
    private DatabaseHelper db;
    private int flightId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_detail);

        // Initialize database
        db = new DatabaseHelper(this);

        // Get data from intent
        flightId = getIntent().getIntExtra("FLIGHT_ID", -1);
        username = getIntent().getStringExtra("USERNAME");

        // Validate data
        if (flightId == -1) {
            Toast.makeText(this, "Invalid flight selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        nameTv = findViewById(R.id.nameTv);
        originTv = findViewById(R.id.originTv);
        destinationTv = findViewById(R.id.destinationTv);
        reserveBtn = findViewById(R.id.reserveBtn);
        viewAllReservationsBtn = findViewById(R.id.viewAllReservationsBtn); // Initialiser le nouveau bouton

        // Load flight details
        loadFlightDetails();

        // Setup reserve button
        reserveBtn.setOnClickListener(v -> makeReservation());

        // Setup view all reservations button
        viewAllReservationsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(FlightDetailActivity.this, ReservationListActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });
    }

    private void loadFlightDetails() {
        Flight flight = db.getFlightById(flightId);

        if (flight != null) {
            nameTv.setText("Flight: " + flight.getName());
            originTv.setText("From: " + flight.getOrigin());
            destinationTv.setText("To: " + flight.getDestination());

            // Check if already reserved
            if (db.hasReservation(flightId, username)) {
                reserveBtn.setText("Already Reserved");
                reserveBtn.setEnabled(false);
            }
        } else {
            nameTv.setText("Flight not found");
            originTv.setText("");
            destinationTv.setText("");
            reserveBtn.setEnabled(false);
        }
    }

    private void makeReservation() {
        // Double-check reservation doesn't exist
        if (db.hasReservation(flightId, username)) {
            Toast.makeText(this, "You already have a reservation for this flight", Toast.LENGTH_SHORT).show();
            reserveBtn.setText("Already Reserved");
            reserveBtn.setEnabled(false);
            return;
        }

        // Create reservation
        boolean success = db.addReservation(flightId, username);

        if (success) {
            Toast.makeText(this, "Reservation created successfully!", Toast.LENGTH_SHORT).show();
            reserveBtn.setText("Reserved!");
            reserveBtn.setEnabled(false);

            // Option: Ouvrir la liste des réservations après réservation
            Intent intent = new Intent(FlightDetailActivity.this, ReservationListActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
        }
    }
}
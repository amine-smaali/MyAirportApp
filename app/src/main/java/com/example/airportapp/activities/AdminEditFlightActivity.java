package com.example.airportapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airportapp.R;
import com.example.airportapp.db.DatabaseHelper;

public class AdminEditFlightActivity extends AppCompatActivity {
    private EditText nameEt;
    private EditText originEt;
    private EditText destinationEt;
    private Button updateBtn;
    private DatabaseHelper db;
    private int flightId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_flight);

        db = new DatabaseHelper(this);

        nameEt = findViewById(R.id.nameEt);
        originEt = findViewById(R.id.originEt);
        destinationEt = findViewById(R.id.destinationEt);
        updateBtn = findViewById(R.id.updateBtn);

        // Récupérer les données du vol
        flightId = getIntent().getIntExtra("FLIGHT_ID", -1);
        String name = getIntent().getStringExtra("FLIGHT_NAME");
        String origin = getIntent().getStringExtra("FLIGHT_ORIGIN");
        String destination = getIntent().getStringExtra("FLIGHT_DESTINATION");

        if (flightId == -1) {
            Toast.makeText(this, "Invalid flight", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Pré-remplir les champs
        nameEt.setText(name);
        originEt.setText(origin);
        destinationEt.setText(destination);

        updateBtn.setOnClickListener(v -> updateFlight());
    }

    private void updateFlight() {
        String name = nameEt.getText().toString().trim();
        String origin = originEt.getText().toString().trim();
        String destination = destinationEt.getText().toString().trim();

        if (name.isEmpty() || origin.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = db.updateFlight(flightId, name, origin, destination);
        if (success) {
            Toast.makeText(this, "Flight updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update flight", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.airportapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airportapp.R;
import com.example.airportapp.db.DatabaseHelper;

public class AdminAddFlightActivity extends AppCompatActivity {
    private EditText nameEt;
    private EditText originEt;
    private EditText destinationEt;
    private Button addBtn , cancelBtn;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_flight);

        db = new DatabaseHelper(this);

        nameEt = findViewById(R.id.nameEt);
        originEt = findViewById(R.id.originEt);
        destinationEt = findViewById(R.id.destinationEt);
        addBtn = findViewById(R.id.addBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        addBtn.setOnClickListener(v -> addFlight());
        cancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminAddFlightActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
        });

    }

    private void addFlight() {
        String name = nameEt.getText().toString().trim();
        String origin = originEt.getText().toString().trim();
        String destination = destinationEt.getText().toString().trim();

        if (name.isEmpty() || origin.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = db.addFlight(name, origin, destination);
        if (success) {
            Toast.makeText(this, "Flight added successfully", Toast.LENGTH_SHORT).show();
            nameEt.setText("");
            originEt.setText("");
            destinationEt.setText("");
        } else {
            Toast.makeText(this, "Failed to add flight", Toast.LENGTH_SHORT).show();
        }
    }
}
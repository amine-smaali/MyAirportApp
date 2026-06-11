package com.example.airportapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.airportapp.R;
import com.example.airportapp.db.DatabaseHelper;

public class AdminDashboardActivity extends AppCompatActivity {
    private TextView welcomeTv;
    private CardView manageFlightsCard;
    private CardView viewReservationsCard;
    private CardView addFlightCard;

    private Button logoutBtn;
    private DatabaseHelper db;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = new DatabaseHelper(this);
        username = getIntent().getStringExtra("USERNAME");

        welcomeTv = findViewById(R.id.welcomeTv);
        manageFlightsCard = findViewById(R.id.manageFlightsCard);
        viewReservationsCard = findViewById(R.id.viewReservationsCard);
        addFlightCard = findViewById(R.id.addFlightCard);
        logoutBtn = findViewById(R.id.logoutBtn);

        welcomeTv.setText("Welcome Admin, " + username + "!");

        // Navigation
        manageFlightsCard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminManageFlightsActivity.class);
            startActivity(intent);
        });

        viewReservationsCard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminViewReservationsActivity.class);
            startActivity(intent);
        });

        addFlightCard.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminAddFlightActivity.class);
            startActivity(intent);
        });



        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
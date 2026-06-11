package com.example.airportapp.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airportapp.R;
import com.example.airportapp.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AdminViewReservationsActivity extends AppCompatActivity {
    private ListView reservationsLv;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_reservations);

        db = new DatabaseHelper(this);
        reservationsLv = findViewById(R.id.reservationsLv);

        loadAllReservations();
    }

    private void loadAllReservations() {
        // Cette méthode devrait exister dans DatabaseHelper
        List<String> allReservations = db.getAllReservations();

        if (allReservations.isEmpty()) {
            Toast.makeText(this, "No reservations found", Toast.LENGTH_SHORT).show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, allReservations);
            reservationsLv.setAdapter(adapter);
        }
    }
}
package com.example.airportapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airportapp.R;
import com.example.airportapp.adapter.FlightAdapter;
import com.example.airportapp.db.DatabaseHelper;
import com.example.airportapp.model.Flight;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FlightAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private FlightAdapter adapter;
    private DatabaseHelper db;
    private String username;
    private List<Flight> flights;
    private Button viewReservationsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get username from intent
        username = getIntent().getStringExtra("USERNAME");
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        viewReservationsBtn = findViewById(R.id.viewReservationsBtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configurer le bouton des réservations
        viewReservationsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReservationListActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });

        loadFlights();
    }

    private void loadFlights() {
        flights = db.getAllFlights();

        if (flights.isEmpty()) {
            Toast.makeText(this, "No flights available", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new FlightAdapter(flights, this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(Flight flight) {
        Intent intent = new Intent(this, FlightDetailActivity.class);
        intent.putExtra("FLIGHT_ID", flight.getId());
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_reservations) {
            Intent intent = new Intent(this, ReservationListActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Rafraîchir la liste des vols si nécessaire
        loadFlights();
    }
}
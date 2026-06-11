package com.example.airportapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.airportapp.R;
import com.example.airportapp.db.DatabaseHelper;
import com.example.airportapp.model.Reservation;

import java.util.List;

public class ReservationListActivity extends AppCompatActivity {
    private ListView reservationsLv;
    private DatabaseHelper db;
    private String username;
    private List<Reservation> reservations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);

        db = new DatabaseHelper(this);
        username = getIntent().getStringExtra("USERNAME");

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        reservationsLv = findViewById(R.id.reservationsLv);
        loadReservations();

        // Ajouter un clic long pour supprimer une réservation
        reservationsLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });
    }

    private void loadReservations() {
        reservations = db.getReservations(username);

        if (reservations.isEmpty()) {
            Toast.makeText(this, "No reservations found", Toast.LENGTH_SHORT).show();
            reservationsLv.setAdapter(null);
        } else {
            String[] items = new String[reservations.size()];
            for (int i = 0; i < reservations.size(); i++) {
                Reservation r = reservations.get(i);
                items[i] = "Reservation #" + r.getId() +
                        " - Flight: " + r.getFlightName() +
                        "\nStatus: Confirmed";
            }

            android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, items);
            reservationsLv.setAdapter(adapter);
        }
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Reservation");
        builder.setMessage("Are you sure you want to cancel this reservation?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteReservation(position);
            }
        });

        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteReservation(int position) {
        if (position < reservations.size()) {
            Reservation reservation = reservations.get(position);
            boolean success = db.deleteReservation(reservation.getId());

            if (success) {
                Toast.makeText(this, "Reservation cancelled successfully", Toast.LENGTH_SHORT).show();
                loadReservations(); // Rafraîchir la liste
            } else {
                Toast.makeText(this, "Failed to cancel reservation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReservations(); // Rafraîchir la liste quand on revient sur l'activité
    }
}
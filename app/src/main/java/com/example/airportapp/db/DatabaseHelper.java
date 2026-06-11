package com.example.airportapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.airportapp.model.Flight;
import com.example.airportapp.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "AirportDB";
    private static final int DATABASE_VERSION = 4; // Incremented to force recreate

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_FLIGHTS = "flights";
    private static final String TABLE_RESERVATIONS = "reservations";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT," +
                "role TEXT DEFAULT 'user')");

        // Create flights table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FLIGHTS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "origin TEXT," +
                "destination TEXT)");

        // Create reservations table
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_RESERVATIONS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "flight_id INTEGER)");

        // Ajouter l'utilisateur admin par défaut
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, password, role) VALUES ('admin', 'admin123', 'admin')");

        // Ajoutez l'utilisateur admin avec rôle
        ContentValues adminValues = new ContentValues();
        adminValues.put("username", "admin");
        adminValues.put("password", "admin123");
        adminValues.put("role", "admin");
        db.insert(TABLE_USERS, null, adminValues);

        ContentValues userValues = new ContentValues();
        userValues.put("username", "user");
        userValues.put("password", "123");
        userValues.put("role", "user");
        db.insert(TABLE_USERS, null, userValues);

        // Add sample data
        addSampleData(db);
    }

    // Méthode pour obtenir le rôle d'un utilisateur

    public String getUserRole(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT role FROM " + TABLE_USERS + " WHERE username = ?",
                    new String[]{username}
            );

            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
            return "user"; // Valeur par défaut
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Supprimez toutes les tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLIGHTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Recréez les tables
        onCreate(db);
    }

    private void addSampleData(SQLiteDatabase db) {
        // Check if data already exists
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count > 0) {
            return; // Data already exists
        }

        // Add test user
        db.execSQL("INSERT INTO " + TABLE_USERS + " (username, password) VALUES ('user', '123')");

        // Add sample flights
        String[] flights = {
                "INSERT INTO " + TABLE_FLIGHTS + " (name, origin, destination) VALUES ('AA101', 'New York', 'London')",
                "INSERT INTO " + TABLE_FLIGHTS + " (name, origin, destination) VALUES ('BA202', 'London', 'Paris')",
                "INSERT INTO " + TABLE_FLIGHTS + " (name, origin, destination) VALUES ('UA303', 'Chicago', 'Miami')",
                "INSERT INTO " + TABLE_FLIGHTS + " (name, origin, destination) VALUES ('DL404', 'Atlanta', 'Seattle')",
                "INSERT INTO " + TABLE_FLIGHTS + " (name, origin, destination) VALUES ('LH505', 'Frankfurt', 'Tokyo')"
        };

        for (String flight : flights) {
            db.execSQL(flight);
        }
    }

    // Méthodes de gestion des vols pour l'admin
    public boolean addFlight(String name, String origin, String destination) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("origin", origin);
        values.put("destination", destination);

        long result = db.insert(TABLE_FLIGHTS, null, values);
        return result != -1;
    }

    public boolean updateFlight(int id, String name, String origin, String destination) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("origin", origin);
        values.put("destination", destination);

        int rowsAffected = db.update(TABLE_FLIGHTS, values, "id = ?",
                new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    public boolean deleteFlight(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // D'abord supprimer les réservations associées
        db.delete(TABLE_RESERVATIONS, "flight_id = ?",
                new String[]{String.valueOf(id)});

        // Puis supprimer le vol
        int rowsAffected = db.delete(TABLE_FLIGHTS, "id = ?",
                new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }

    // Dans DatabaseHelper.java
    public List<String> getAllReservations() {
        List<String> reservationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r.id, u.username, f.name, f.origin, f.destination " +
                "FROM " + TABLE_RESERVATIONS + " r " +
                "JOIN " + TABLE_USERS + " u ON r.username = u.username " +
                "JOIN " + TABLE_FLIGHTS + " f ON r.flight_id = f.id " +
                "ORDER BY r.id DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String reservation = "ID: " + cursor.getInt(0) +
                        " | User: " + cursor.getString(1) +
                        " | Flight: " + cursor.getString(2) +
                        " (" + cursor.getString(3) + " → " + cursor.getString(4) + ")";
                reservationList.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reservationList;
    }

    // Dans DatabaseHelper.java
    public int getUsersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getFlightsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_FLIGHTS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getReservationsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_RESERVATIONS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public boolean deleteReservation(int reservationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_RESERVATIONS, "id = ?",
                new String[]{String.valueOf(reservationId)});
        return rowsAffected > 0;
    }

    // USER OPERATIONS
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS +
                        " WHERE username = ? AND password = ?",
                new String[]{username, password}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // FLIGHT OPERATIONS
    public List<Flight> getAllFlights() {
        List<Flight> flightList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FLIGHTS, null);

        if (cursor.moveToFirst()) {
            do {
                Flight flight = new Flight(
                        cursor.getInt(0),    // id
                        cursor.getString(1), // name
                        cursor.getString(2), // origin
                        cursor.getString(3)  // destination
                );
                flightList.add(flight);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return flightList;
    }

    public Flight getFlightById(int flightId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_FLIGHTS + " WHERE id = ?",
                new String[]{String.valueOf(flightId)}
        );

        Flight flight = null;
        if (cursor.moveToFirst()) {
            flight = new Flight(
                    cursor.getInt(0),    // id
                    cursor.getString(1), // name
                    cursor.getString(2), // origin
                    cursor.getString(3)  // destination
            );
        }
        cursor.close();
        return flight;
    }

    // RESERVATION OPERATIONS
    public boolean addReservation(int flightId, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("flight_id", flightId);

        long result = db.insert(TABLE_RESERVATIONS, null, values);
        return result != -1;
    }

    public List<Reservation> getReservations(String username) {
        List<Reservation> reservationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r.id, f.name, f.origin, f.destination FROM " + TABLE_RESERVATIONS + " r " +
                "JOIN " + TABLE_FLIGHTS + " f ON r.flight_id = f.id " +
                "WHERE r.username = ? ORDER BY r.id DESC";

        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                Reservation reservation = new Reservation(
                        cursor.getInt(0),    // id
                        cursor.getString(1), // flightName
                        cursor.getString(2), // origin
                        cursor.getString(3)  // destination
                );
                reservationList.add(reservation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reservationList;
    }

    public boolean hasReservation(int flightId, String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_RESERVATIONS +
                        " WHERE flight_id = ? AND username = ?",
                new String[]{String.valueOf(flightId), username}
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Vérifier les sièges disponibles (si vous avez une table de sièges)
    public int getAvailableSeats(int flightId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Cette requête suppose que vous avez une table des sièges
        // Pour l'instant, retourner un nombre arbitraire
        return 10; // À adapter selon votre structure
    }

    // Obtenir le nombre total de réservations d'un utilisateur
    public int getReservationCount(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_RESERVATIONS +
                        " WHERE username = ?",
                new String[]{username});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
package com.example.airportapp.model;

public class Reservation {
    private int id;
    private String flightName;
    private String origin;
    private String destination;

    public Reservation(int id, String flightName) {
        this.id = id;
        this.flightName = flightName;
    }

    public Reservation(int id, String flightName, String origin, String destination) {
        this.id = id;
        this.flightName = flightName;
        this.origin = origin;
        this.destination = destination;
    }

    public int getId() {
        return id;
    }

    public String getFlightName() {
        return flightName;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }
}
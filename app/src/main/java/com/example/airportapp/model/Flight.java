package com.example.airportapp.model;

public class Flight {
    private int id;
    private String name;
    private String origin;
    private String destination;

    public Flight(int id, String name, String origin, String destination) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.destination = destination;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }
}
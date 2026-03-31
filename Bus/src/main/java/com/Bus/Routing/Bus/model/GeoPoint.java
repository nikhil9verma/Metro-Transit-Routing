package com.Bus.Routing.Bus.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeoPoint {

    private double lat;
    private double lon;

    public GeoPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}


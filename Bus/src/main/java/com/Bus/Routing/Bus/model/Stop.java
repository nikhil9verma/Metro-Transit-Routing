package com.Bus.Routing.Bus.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stop {

    public final int id;
    public final String name;
    public final double lat;
    public final double lon;

    public Stop(int id, String name, double lat, double lon) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }
}


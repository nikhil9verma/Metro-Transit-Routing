package com.Bus.Routing.Bus.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StopTime {
    public final int stopId;
    public final int arrivalMin;
    public final int departureMin;

    public StopTime(int stopId, int arrivalMin, int departureMin) {
        this.stopId = stopId;
        this.arrivalMin = arrivalMin;
        this.departureMin = departureMin;
    }
}
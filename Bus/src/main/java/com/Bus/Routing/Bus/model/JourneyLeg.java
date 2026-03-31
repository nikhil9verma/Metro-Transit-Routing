package com.Bus.Routing.Bus.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JourneyLeg {
    public String routeId;
    public int boardStopId;
    public int alightStopId;
    public int departureMin;
    public int arrivalMin;

    public JourneyLeg(String routeId, int boardStopId, int alightStopId, int departureMin, int arrivalMin) {
        this.routeId = routeId;
        this.boardStopId = boardStopId;
        this.alightStopId = alightStopId;
        this.departureMin = departureMin;
        this.arrivalMin = arrivalMin;
    }
}

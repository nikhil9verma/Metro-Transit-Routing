package com.Bus.Routing.Bus.model;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Journey {
    public int arrivalMin;
    public int durationMin;
    public int transfers;
    public List<JourneyLeg> legs;

    public Journey(int arrivalMin, int durationMin, int transfers, List<JourneyLeg> legs) {
        this.arrivalMin = arrivalMin;
        this.durationMin = durationMin;
        this.transfers = transfers;
        this.legs = legs;
    }
}
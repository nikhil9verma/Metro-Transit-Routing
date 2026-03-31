package com.Bus.Routing.Bus.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JourneyDTO {
    public String arrivalTime;
    public int durationMin;
    public int transfers;
    public List<JourneyLegDTO> legs;

    public JourneyDTO(String arrivalTime, int durationMin, int transfers, List<JourneyLegDTO> legs) {
        this.arrivalTime = arrivalTime;
        this.durationMin = durationMin;
        this.transfers = transfers;
        this.legs = legs;
    }
}

package com.Bus.Routing.Bus.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RouteResponse {
    public String currentTime;
    public List<JourneyDTO> journeys;

    public RouteResponse(String currentTime, List<JourneyDTO> journeys) {
        this.currentTime = currentTime;
        this.journeys = journeys;
    }
}

package com.Bus.Routing.Bus.dto;

import java.util.List;

public class RouteResponse {
    public String currentTime;
    public List<JourneyDTO> journeys;

    public RouteResponse(String currentTime, List<JourneyDTO> journeys) {
        this.currentTime = currentTime;
        this.journeys = journeys;
    }
}

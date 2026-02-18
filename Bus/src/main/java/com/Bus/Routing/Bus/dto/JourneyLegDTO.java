package com.Bus.Routing.Bus.dto;

public class JourneyLegDTO {
    public String routeId;
    public String boardStop;
    public String alightStop;
    public String departure;
    public String arrival;

    public JourneyLegDTO(String routeId, String boardStop, String alightStop,
                         String departure, String arrival) {
        this.routeId = routeId;
        this.boardStop = boardStop;
        this.alightStop = alightStop;
        this.departure = departure;
        this.arrival = arrival;
    }
}

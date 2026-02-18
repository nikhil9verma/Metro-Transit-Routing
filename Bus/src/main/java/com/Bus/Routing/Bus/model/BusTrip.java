package com.Bus.Routing.Bus.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusTrip {
    public final String tripId;
    public final String routeId;
    public final List<StopTime> stopTimes;

    public BusTrip(String tripId, String routeId, List<StopTime> stopTimes) {
        this.tripId = tripId;
        this.routeId = routeId;
        this.stopTimes = stopTimes;
    }
}


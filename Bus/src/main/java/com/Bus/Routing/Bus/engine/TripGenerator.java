package com.Bus.Routing.Bus.engine;


import com.Bus.Routing.Bus.model.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TripGenerator {

    private final TransitData data;
    private final TimeEngine timeEngine;
    private final List<BusTrip> allTrips = new ArrayList<>();
    private final Map<String, List<BusTrip>> tripsByRoute = new HashMap<>();
    private final Map<Integer, List<int[]>> stopTripIndex = new HashMap<>();

    public TripGenerator(TransitData data, TimeEngine timeEngine) {
        this.data = data;
        this.timeEngine = timeEngine;
        generate();
    }

    private void generate() {
        int tripCounter = 0;
        for (RouteTemplate rt : data.getRouteTemplates()) {
            tripCounter = generateDirectional(rt, rt.stopIds, rt.routeId, tripCounter);
            List<Integer> reversed = new ArrayList<>(rt.stopIds);
            Collections.reverse(reversed);
            tripCounter = generateDirectional(rt, reversed, rt.routeId + "_R", tripCounter);
        }
        buildStopTripIndex();
    }

    private int generateDirectional(RouteTemplate rt, List<Integer> stops,
                                    String routeId, int tripCounter) {
        tripsByRoute.put(routeId, new ArrayList<>());
        int currentStart = TimeEngine.SERVICE_START;
        while (currentStart <= TimeEngine.SERVICE_END) {
            List<StopTime> stopTimes = new ArrayList<>();
            int time = currentStart;
            for (int i = 0; i < stops.size(); i++) {
                stopTimes.add(new StopTime(stops.get(i), time, time));
                if (i < stops.size() - 1) {
                    time += timeEngine.segmentDuration(rt.type, rt.isExpress, time);
                }
            }
            String tripId = routeId + "_T" + tripCounter++;
            BusTrip trip = new BusTrip(tripId, routeId, stopTimes);
            allTrips.add(trip);
            tripsByRoute.get(routeId).add(trip);
            currentStart += timeEngine.headwayMinutes(currentStart);
        }
        return tripCounter;
    }

    private void buildStopTripIndex() {
        for (int i = 0; i < allTrips.size(); i++) {
            BusTrip trip = allTrips.get(i);
            for (int seq = 0; seq < trip.stopTimes.size(); seq++) {
                int stopId = trip.stopTimes.get(seq).stopId;
                stopTripIndex.computeIfAbsent(stopId, k -> new ArrayList<>())
                        .add(new int[]{i, seq});
            }
        }
    }

    public List<BusTrip> getAllTrips() { return allTrips; }
    public Map<String, List<BusTrip>> getTripsByRoute() { return tripsByRoute; }
    public Map<Integer, List<int[]>> getStopTripIndex() { return stopTripIndex; }
}

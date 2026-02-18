package com.Bus.Routing.Bus.service;

import com.Bus.Routing.Bus.dto.RouteRequest;
import com.Bus.Routing.Bus.dto.RouteResponse;
import com.Bus.Routing.Bus.engine.RaptorEngine;
import com.Bus.Routing.Bus.engine.TimeEngine;
import org.springframework.stereotype.Service;


import com.Bus.Routing.Bus.dto.*;
import com.Bus.Routing.Bus.engine.*;
import com.Bus.Routing.Bus.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteService {

    private final TransitData transitData;
    private final TimeEngine timeEngine;
    private final RaptorEngine raptorEngine;
    private final GeometryEngine geometryEngine;

    public RouteService(TransitData transitData, TimeEngine timeEngine,
                        RaptorEngine raptorEngine, GeometryEngine geometryEngine) {
        this.transitData    = transitData;
        this.timeEngine     = timeEngine;
        this.raptorEngine   = raptorEngine;
        this.geometryEngine = geometryEngine;
    }

    public RouteResponse findRoutes(RouteRequest req) {
        int depMin = timeEngine.parseTime(req.getTime());
        List<Journey> journeys = raptorEngine.route(req.getSourceId(), req.getDestinationId(), depMin);

        List<JourneyDTO> dtos = new ArrayList<>();
        for (Journey j : journeys) {
            List<JourneyLegDTO> legDTOs = new ArrayList<>();
            for (JourneyLeg leg : j.legs) {
                Stop board  = transitData.getStop(leg.boardStopId);
                Stop alight = transitData.getStop(leg.alightStopId);
                legDTOs.add(new JourneyLegDTO(
                        leg.routeId,
                        board  != null ? board.name  : String.valueOf(leg.boardStopId),
                        alight != null ? alight.name : String.valueOf(leg.alightStopId),
                        timeEngine.formatTime(leg.departureMin),
                        timeEngine.formatTime(leg.arrivalMin)
                ));
            }
            dtos.add(new JourneyDTO(
                    timeEngine.formatTime(j.arrivalMin),
                    j.durationMin,
                    j.transfers,
                    legDTOs
            ));
        }
        return new RouteResponse(req.getTime(), dtos);
    }

    public List<Stop> getAllStops() { return transitData.getStops(); }

    public List<GeoPoint> getRouteGeometry(String routeId) {
        return geometryEngine.getGeometry(routeId);
    }
}

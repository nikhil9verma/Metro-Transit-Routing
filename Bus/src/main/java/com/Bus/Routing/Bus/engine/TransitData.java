package com.Bus.Routing.Bus.engine;


import com.Bus.Routing.Bus.model.RouteTemplate;
import com.Bus.Routing.Bus.model.Stop;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TransitData {

    private final List<Stop> stops = new ArrayList<>();
    private final List<RouteTemplate> routeTemplates = new ArrayList<>();
    private final Map<Integer, Stop> stopMap = new HashMap<>();

    public TransitData() {
        initStops();
        initRoutes();
    }

    private void initStops() {
        stops.add(new Stop(1,  "Central Station",    30.7333, 76.7794));
        stops.add(new Stop(2,  "City Hall",          30.7412, 76.7856));
        stops.add(new Stop(3,  "University Quarter", 30.7501, 76.7920));
        stops.add(new Stop(4,  "Tech Park",          30.7580, 76.8010));
        stops.add(new Stop(5,  "North Market",       30.7650, 76.7850));
        stops.add(new Stop(6,  "Industrial Zone",    30.7200, 76.7680));
        stops.add(new Stop(7,  "Airport Road",       30.7100, 76.7550));
        stops.add(new Stop(8,  "Hospital Crossing",  30.7450, 76.7700));
        stops.add(new Stop(9,  "Heritage Gate",      30.7380, 76.7640));
        stops.add(new Stop(10, "Sports Complex",     30.7530, 76.7780));
        stops.add(new Stop(11, "West End",           30.7310, 76.7500));
        stops.add(new Stop(12, "East Colony",        30.7480, 76.8100));
        stops.add(new Stop(13, "River Bridge",       30.7260, 76.7900));
        stops.add(new Stop(14, "Green Park",         30.7600, 76.8080));
        stops.add(new Stop(15, "Mall Road",          30.7350, 76.7980));
        stops.add(new Stop(16, "Sector 17 Plaza",    30.7420, 76.7860));
        stops.add(new Stop(17, "Bus Depot",          30.7180, 76.7750));
        stops.add(new Stop(18, "Panchkula Border",   30.7700, 76.8150));
        stops.add(new Stop(19, "Zirakpur Gate",      30.7050, 76.7600));
        stops.add(new Stop(20, "Mohali Sector 70",   30.7150, 76.8050));

        for (Stop s : stops) stopMap.put(s.id, s);
    }

    private void initRoutes() {
        routeTemplates.add(new RouteTemplate("R1",  Arrays.asList(1, 2, 3, 4, 5),       false, "medium"));
        routeTemplates.add(new RouteTemplate("R2",  Arrays.asList(1, 9, 11, 6, 7),      false, "medium"));
        routeTemplates.add(new RouteTemplate("R3",  Arrays.asList(2, 8, 10, 4, 14),     false, "medium"));
        routeTemplates.add(new RouteTemplate("R4",  Arrays.asList(1, 15, 12, 4),        false, "short"));
        routeTemplates.add(new RouteTemplate("R5",  Arrays.asList(7, 6, 13, 1, 2),      false, "long"));
        routeTemplates.add(new RouteTemplate("R6",  Arrays.asList(5, 4, 12, 14, 18),    false, "medium"));
        routeTemplates.add(new RouteTemplate("R7",  Arrays.asList(19, 7, 6, 17, 1),     false, "long"));
        routeTemplates.add(new RouteTemplate("R8",  Arrays.asList(20, 13, 9, 16, 2),    false, "medium"));
        routeTemplates.add(new RouteTemplate("EX1", Arrays.asList(1, 4, 18),            true,  "long"));
        routeTemplates.add(new RouteTemplate("EX2", Arrays.asList(7, 1, 5),             true,  "medium"));
    }

    public List<Stop> getStops() { return stops; }
    public List<RouteTemplate> getRouteTemplates() { return routeTemplates; }
    public Map<Integer, Stop> getStopMap() { return stopMap; }
    public Stop getStop(int id) { return stopMap.get(id); }
}

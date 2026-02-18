package com.Bus.Routing.Bus.engine;


import com.Bus.Routing.Bus.model.GeoPoint;
import com.Bus.Routing.Bus.model.RouteTemplate;
import com.Bus.Routing.Bus.model.Stop;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GeometryEngine {

    private final TransitData data;
    private final Map<String, List<GeoPoint>> geometries = new HashMap<>();

    public GeometryEngine(TransitData data) {
        this.data = data;
        buildGeometries();
    }

    private void buildGeometries() {
        for (RouteTemplate rt : data.getRouteTemplates()) {
            geometries.put(rt.routeId, buildPolyline(rt.stopIds));
            List<Integer> rev = new ArrayList<>(rt.stopIds);
            Collections.reverse(rev);
            geometries.put(rt.routeId + "_R", buildPolyline(rev));
        }
    }

    private List<GeoPoint> buildPolyline(List<Integer> stopIds) {
        List<GeoPoint> points = new ArrayList<>();
        for (int i = 0; i < stopIds.size() - 1; i++) {
            Stop from = data.getStop(stopIds.get(i));
            Stop to   = data.getStop(stopIds.get(i + 1));
            interpolate(from.lat, from.lon, to.lat, to.lon, points);
        }
        Stop last = data.getStop(stopIds.get(stopIds.size() - 1));
        points.add(new GeoPoint(last.lat, last.lon));
        return points;
    }

    private void interpolate(double lat1, double lon1, double lat2, double lon2,
                             List<GeoPoint> out) {
        double totalMeters = haversine(lat1, lon1, lat2, lon2);
        int steps = Math.max(1, (int)(totalMeters / 10.0));
        for (int i = 0; i < steps; i++) {
            double t = (double) i / steps;
            out.add(new GeoPoint(lat1 + t * (lat2 - lat1), lon1 + t * (lon2 - lon1)));
        }
    }

    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    public List<GeoPoint> getGeometry(String routeId) {
        return geometries.getOrDefault(routeId, Collections.emptyList());
    }
}

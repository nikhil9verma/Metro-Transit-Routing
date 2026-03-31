package com.Bus.Routing.Bus.controller;



import com.Bus.Routing.Bus.dto.RouteRequest;
import com.Bus.Routing.Bus.dto.RouteResponse;
import com.Bus.Routing.Bus.model.GeoPoint;
import com.Bus.Routing.Bus.model.Stop;
import com.Bus.Routing.Bus.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/route")
    public ResponseEntity<RouteResponse> findRoute(@RequestBody RouteRequest req) {
        return ResponseEntity.ok(routeService.findRoutes(req));
    }

    @GetMapping("/stops")
    public ResponseEntity<List<Stop>> getStops() {
        return ResponseEntity.ok(routeService.getAllStops());
    }

    @GetMapping("/route-geometry/{routeId}")
    public ResponseEntity<List<GeoPoint>> getGeometry(@PathVariable String routeId) {
        return ResponseEntity.ok(routeService.getRouteGeometry(routeId));
    }
}

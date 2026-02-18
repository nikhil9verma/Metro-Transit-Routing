package com.Bus.Routing.Bus.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteTemplate {
    public final String routeId;
    public final List<Integer> stopIds;
    public final boolean isExpress;
    public final String type;

    public RouteTemplate(String routeId, List<Integer> stopIds, boolean isExpress, String type) {
        this.routeId = routeId;
        this.stopIds = stopIds;
        this.isExpress = isExpress;
        this.type = type;
    }
}

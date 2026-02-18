package com.Bus.Routing.Bus.dto;


public class RouteRequest {
    private int sourceId;
    private int destinationId;
    private String time;

    public int getSourceId() { return sourceId; }
    public void setSourceId(int sourceId) { this.sourceId = sourceId; }
    public int getDestinationId() { return destinationId; }
    public void setDestinationId(int destinationId) { this.destinationId = destinationId; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}

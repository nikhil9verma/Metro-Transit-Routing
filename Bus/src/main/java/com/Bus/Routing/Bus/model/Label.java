package com.Bus.Routing.Bus.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Label {

    public int    arrivalMin;
    public int    transfers;
    public int    boardStop;
    public int    alightStop;
    public String routeId;
    public String tripId;
    public Label  prev;

    public Label(int arrivalMin, int transfers) {
        this.arrivalMin = arrivalMin;
        this.transfers  = transfers;
    }

    public int    getArrivalMin()  { return arrivalMin;  }
    public int    getTransfers()   { return transfers;   }
    public int    getBoardStop()   { return boardStop;   }
    public int    getAlightStop()  { return alightStop;  }
    public String getRouteId()     { return routeId;     }
    public String getTripId()      { return tripId;      }
    public Label  getPrev()        { return prev;        }

    public void setArrivalMin(int v)    { this.arrivalMin = v; }
    public void setTransfers(int v)     { this.transfers  = v; }
    public void setBoardStop(int v)     { this.boardStop  = v; }
    public void setAlightStop(int v)    { this.alightStop = v; }
    public void setRouteId(String v)    { this.routeId    = v; }
    public void setTripId(String v)     { this.tripId     = v; }
    public void setPrev(Label v)        { this.prev       = v; }
}
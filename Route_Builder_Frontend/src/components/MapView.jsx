import React, { useEffect, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { fetchGeometry } from '../api';

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon-2x.png',
  iconUrl:       'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-icon.png',
  shadowUrl:     'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png',
});

export default function MapView({ stops, selectedJourney }) {
  const mapRef       = useRef(null);
  const mapInstance  = useRef(null);
  const stopLayer    = useRef(null);
  const routeLayer   = useRef(null);

  useEffect(() => {
    if (mapInstance.current) return;
    mapInstance.current = L.map(mapRef.current, {
      center: [30.7400, 76.7850],
      zoom: 13,
    });
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
      maxZoom: 18,
    }).addTo(mapInstance.current);
    stopLayer.current  = L.layerGroup().addTo(mapInstance.current);
    routeLayer.current = L.layerGroup().addTo(mapInstance.current);
  }, []);

  useEffect(() => {
    if (!mapInstance.current || !stops.length) return;
    stopLayer.current.clearLayers();
    stops.forEach(stop => {
      L.circleMarker([stop.lat, stop.lon], {
        radius: 6, fillColor: '#00d4b4',
        color: '#fff', weight: 2, fillOpacity: 0.9,
      })
        .addTo(stopLayer.current)
        .bindTooltip(stop.name, { direction: 'top' });
    });
  }, [stops]);

  useEffect(() => {
  if (!mapInstance.current) return;
  routeLayer.current.clearLayers();
  if (!selectedJourney) return;

  const colors = ['#ff6b35', '#f7c59f', '#efefd0', '#004e89', '#1a936f'];

  const stopNameToCoord = {};
  stops.forEach(s => {
    stopNameToCoord[s.name] = [s.lat, s.lon];
  });

  const sourceName = selectedJourney.legs[0]?.boardStop;
  const destinationName =
    selectedJourney.legs[selectedJourney.legs.length - 1]?.alightStop;

  const transferNames = new Set(
    selectedJourney.legs.slice(0, -1).map(l => l.alightStop)
  );

  // Draw routes
  selectedJourney.legs.forEach((leg, i) => {
    fetchGeometry(leg.routeId)
      .then(pts => {
        if (pts && pts.length) {
          L.polyline(
            pts.map(p => [p.lat, p.lon]),
            {
              color: colors[i % colors.length],
              weight: 4,
              opacity: 0.85,
            }
          ).addTo(routeLayer.current);
        }
      })
      .catch(() => {});
  });

  // 🔵 SOURCE Marker
  if (sourceName && stopNameToCoord[sourceName]) {
    L.circleMarker(stopNameToCoord[sourceName], {
      radius: 8,
      fillColor: '#007bff',
      color: '#fff',
      weight: 3,
      fillOpacity: 1,
    })
      .addTo(routeLayer.current)
      .bindTooltip(`Source: ${sourceName}`, { direction: 'top' });
  }

  // 🔴 DESTINATION Marker
  if (destinationName && stopNameToCoord[destinationName]) {
    L.circleMarker(stopNameToCoord[destinationName], {
      radius: 8,
      fillColor: '#ff0000',
      color: '#fff',
      weight: 3,
      fillOpacity: 1,
    })
      .addTo(routeLayer.current)
      .bindTooltip(`Destination: ${destinationName}`, { direction: 'top' });
  }

  // 🟠 Transfer markers
  transferNames.forEach(name => {
    if (name === destinationName) return; // avoid double marking
    const coord = stopNameToCoord[name];
    if (coord) {
      L.circleMarker(coord, {
        radius: 10,
        fillColor: '#ff6b35',
        color: '#fff',
        weight: 2,
        fillOpacity: 1,
      })
        .addTo(routeLayer.current)
        .bindTooltip(`Transfer: ${name}`, { direction: 'top' });
    }
  });
}, [selectedJourney, stops]);


  return <div ref={mapRef} className="map-container" />;
}
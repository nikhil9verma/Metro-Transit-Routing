import axios from 'axios';

const BASE = 'http://localhost:8080/api';

export const fetchStops    = () =>
  axios.get(`${BASE}/stops`).then(r => r.data);

export const fetchRoutes   = (sourceId, destinationId, time) =>
  axios.post(`${BASE}/route`, { sourceId, destinationId, time }).then(r => r.data);

export const fetchGeometry = (routeId) =>
  axios.get(`${BASE}/route-geometry/${routeId}`).then(r => r.data);
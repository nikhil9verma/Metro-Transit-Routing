import axios from 'axios';

const BASE = import.meta.env.VITE_API_BASE_URL;

export const fetchStops = () =>
  axios.get(`${BASE}/stops`).then(r => r.data);

export const fetchRoutes = (sourceId, destinationId, time) =>
  axios.post(`${BASE}/route`, { sourceId, destinationId, time }).then(r => r.data);

export const fetchGeometry = (routeId) =>
  axios.get(`${BASE}/route-geometry/${routeId}`).then(r => r.data);

export const sendChatMessage = (message, time) =>
  axios.post(`${BASE}/chat`, { message, time }).then(r => r.data);

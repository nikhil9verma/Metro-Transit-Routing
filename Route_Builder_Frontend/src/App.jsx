import { useEffect, useState } from 'react';
import RouteForm from './components/RouteForm';
import RouteCard from './components/RouteCard';
import MapView   from './components/MapView';
import ChatBot   from './components/Chatbot';   // ✅ FIXED CASE
import { fetchStops, fetchRoutes } from './api';
import './styles.css';

export default function App() {
  const [stops, setStops] = useState([]);
  const [journeys, setJourneys] = useState([]);
  const [selectedIdx, setSelectedIdx] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchStops()
      .then(setStops)
      .catch(() =>
        setError('Cannot connect to backend. Is it running on port 8080?')
      );
  }, []);

  const handleSearch = async (sourceId, destId, time) => {
    setLoading(true);
    setError('');
    setJourneys([]);
    setSelectedIdx(null);

    try {
      const data = await fetchRoutes(sourceId, destId, time);

      if (!data.journeys || data.journeys.length === 0) {
        setError('No routes found between these stops at this time.');
      } else {
        setJourneys(data.journeys);
        setSelectedIdx(0);
      }
    } catch {
      setError('Failed to fetch routes. Check backend connection.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="header">
        <div className="header-icon">🚌</div>
        <h1>
          Metro <span>Transit</span> Routing
        </h1>
      </div>

      <RouteForm
        stops={stops}
        onSearch={handleSearch}
        loading={loading}
      />

      {error && <div className="error-bar">⚠ {error}</div>}

      <div className="main-layout">
        <div className="sidebar">
          <div className="sidebar-title">
            {journeys.length > 0
              ? `${journeys.length} Routes Found`
              : 'Route Options'}
          </div>

          {journeys.length === 0 && !loading && (
            <div className="empty-state">
              <div className="empty-icon">🗺</div>
              <p>
                Select origin &amp; destination
                <br />
                then click Find Route
              </p>
            </div>
          )}

          {journeys.map((j, i) => (
            <RouteCard
              key={i}
              journey={j}
              index={i}
              selected={selectedIdx === i}
              onSelect={() => setSelectedIdx(i)}
            />
          ))}
        </div>

        <MapView
          stops={stops}
          selectedJourney={
            selectedIdx !== null ? journeys[selectedIdx] : null
          }
        />
      </div>

      {/* ✅ Chatbot Rendered */}
      <ChatBot />
    </div>
  );
}

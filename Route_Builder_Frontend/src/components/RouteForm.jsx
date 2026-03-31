import React, { useState } from 'react';

export default function RouteForm({ stops, onSearch, loading }) {
  const [sourceId, setSourceId] = useState('');
  const [destId,   setDestId]   = useState('');
  const [time,     setTime]     = useState('08:30');

  const handleSubmit = () => {
    if (!sourceId || !destId || sourceId === destId) return;
    onSearch(parseInt(sourceId), parseInt(destId), time);
  };

  return (
    <div className="route-form">
      <div className="form-row">
        <div className="field-group">
          <label>Origin</label>
          <select value={sourceId} onChange={e => setSourceId(e.target.value)}>
            <option value="">— Select Stop —</option>
            {stops.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
          </select>
        </div>
        <div className="arrow-sep">→</div>
        <div className="field-group">
          <label>Destination</label>
          <select value={destId} onChange={e => setDestId(e.target.value)}>
            <option value="">— Select Stop —</option>
            {stops.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
          </select>
        </div>
        <div className="field-group time-field">
          <label>Departure</label>
          <input type="time" value={time} onChange={e => setTime(e.target.value)} />
        </div>
        <button className="find-btn" onClick={handleSubmit} disabled={loading}>
          {loading ? <span className="spinner" /> : 'Find Route'}
        </button>
      </div>
    </div>
  );
}
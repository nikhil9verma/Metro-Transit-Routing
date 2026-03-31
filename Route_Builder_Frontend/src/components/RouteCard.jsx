import React from 'react';

export default function RouteCard({ journey, index, selected, onSelect }) {
  const badges = ['FASTEST', 'FEWEST STOPS', 'DIRECT'];

  return (
    <div className={`route-card ${selected ? 'selected' : ''}`} onClick={onSelect}>
      <div className="card-header">
        <div className="card-timing">
          <span className="arrival-time">{journey.arrivalTime}</span>
          <span className="duration">{journey.durationMin} min</span>
        </div>
        <div className="card-meta">
          {badges[index] && <span className="badge">{badges[index]}</span>}
          <span className="transfers">
            {journey.transfers === 0
              ? 'Direct'
              : `${journey.transfers} transfer${journey.transfers > 1 ? 's' : ''}`}
          </span>
        </div>
      </div>
      <div className="legs">
        {journey.legs.map((leg, i) => (
          <React.Fragment key={i}>
            <div className="leg">
              <div className="leg-route">{leg.routeId}</div>
              <div className="leg-stops">
                <span className="leg-stop">
                  <span className="dot board-dot" />
                  {leg.boardStop}
                  <span className="leg-time">{leg.departure}</span>
                </span>
                <div className="leg-line" />
                <span className="leg-stop">
                  <span className="dot alight-dot" />
                  {leg.alightStop}
                  <span className="leg-time">{leg.arrival}</span>
                </span>
              </div>
            </div>
            {i < journey.legs.length - 1 && (
              <div className="transfer-marker">⇄ Transfer</div>
            )}
          </React.Fragment>
        ))}
      </div>
    </div>
  );
}
package com.Bus.Routing.Bus.engine;


import com.Bus.Routing.Bus.model.*;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class RaptorEngine {

    private static final int MAX_TRANSFERS   = 4;
    private static final int MAX_DURATION    = 240;
    private static final int TRANSFER_BUFFER = 2;
    private static final int INF             = Integer.MAX_VALUE / 2;
    private static final int MAX_RESULTS     = 3;

    private final TripGenerator tripGen;
    private final TransitData   data;

    public RaptorEngine(TripGenerator tripGen, TransitData data) {
        this.tripGen = tripGen;
        this.data    = data;
    }

    public List<Journey> route(int sourceId, int destId, int departureMin) {

        // ── index stops ──────────────────────────────────────────────
        List<Integer> stopIds = new ArrayList<>(data.getStopMap().keySet());
        Collections.sort(stopIds);
        int numStops = stopIds.size();
        Map<Integer, Integer> stopIndex = new HashMap<>();
        for (int i = 0; i < numStops; i++) stopIndex.put(stopIds.get(i), i);

        int srcIdx = stopIndex.get(sourceId);
        int dstIdx = stopIndex.get(destId);

        // ── bestArr[round][stop] = earliest arrival dominating that slot ──
        int[][] bestArr = new int[MAX_TRANSFERS + 1][numStops];
        for (int[] row : bestArr) Arrays.fill(row, INF);
        bestArr[0][srcIdx] = departureMin;

        // ── ONE best label per (round, stop) for propagation ─────────
        Label[][] bestLabel = new Label[MAX_TRANSFERS + 1][numStops];

        // ── ALL labels that reach destination, for result collection ──
        List<Label> destCandidates = new ArrayList<>();

        // ── RAPTOR rounds ─────────────────────────────────────────────
        for (int round = 0; round <= MAX_TRANSFERS; round++) {

            // inherit best arrivals from previous round
            if (round > 0) {
                for (int s = 0; s < numStops; s++) {
                    if (bestArr[round - 1][s] < bestArr[round][s]) {
                        bestArr[round][s]  = bestArr[round - 1][s];
                        bestLabel[round][s] = bestLabel[round - 1][s];
                    }
                }
            }

            boolean improved = false;

            for (Map.Entry<String, List<BusTrip>> entry : tripGen.getTripsByRoute().entrySet()) {
                List<BusTrip> trips = entry.getValue();
                if (trips.isEmpty()) continue;
                int numStopsOnRoute = trips.get(0).stopTimes.size();

                for (int seq = 0; seq < numStopsOnRoute; seq++) {
                    int boardStopId = trips.get(0).stopTimes.get(seq).stopId;
                    Integer boardIdxObj = stopIndex.get(boardStopId);
                    if (boardIdxObj == null) continue;
                    int boardIdx = boardIdxObj;

                    int earliestAtBoard = bestArr[round][boardIdx];
                    if (earliestAtBoard == INF) continue;

                    // transfer buffer — waived at source in round 0
                    int boardTime = earliestAtBoard;
                    if (!(round == 0 && boardIdx == srcIdx)) boardTime += TRANSFER_BUFFER;

                    int tripIdx = binarySearchTrip(trips, seq, boardTime);
                    if (tripIdx < 0) continue;

                    BusTrip boardedTrip = trips.get(tripIdx);

                    for (int s2 = seq + 1; s2 < boardedTrip.stopTimes.size(); s2++) {
                        StopTime st = boardedTrip.stopTimes.get(s2);
                        Integer alightIdxObj = stopIndex.get(st.stopId);
                        if (alightIdxObj == null) continue;
                        int alightIdx    = alightIdxObj;
                        int arrivalAtAlight = st.arrivalMin;

                        if (arrivalAtAlight - departureMin > MAX_DURATION) continue;

                        // build candidate label (always — for dest collection)
                        Label lbl = new Label(arrivalAtAlight, round);
                        lbl.setBoardStop(boardStopId);
                        lbl.setAlightStop(st.stopId);
                        lbl.setRouteId(boardedTrip.routeId);
                        lbl.setTripId(boardedTrip.tripId);
                        lbl.setPrev(bestLabel[round][boardIdx]);

                        // collect every label that reaches destination
                        if (alightIdx == dstIdx) {
                            destCandidates.add(lbl);
                        }

                        // only update propagation state if this beats current best
                        if (arrivalAtAlight < bestArr[round][alightIdx]) {
                            bestArr[round][alightIdx]  = arrivalAtAlight;
                            bestLabel[round][alightIdx] = lbl;
                            improved = true;
                        }
                    }
                }
            }
            if (!improved) break;
        }

        // ── collect & deduplicate results ─────────────────────────────
        Set<String>   seen    = new HashSet<>();
        List<Journey> results = new ArrayList<>();

        // sort candidates: fewer transfers first, then earlier arrival
        destCandidates.sort(Comparator
                .comparingInt(Label::getTransfers)
                .thenComparingInt(Label::getArrivalMin));

        for (Label candidate : destCandidates) {
            if (results.size() >= MAX_RESULTS) break;

            Journey j = reconstruct(candidate, sourceId, departureMin);
            if (j == null) continue;

            String key = journeyKey(j);
            if (seen.add(key)) {
                results.add(j);
            }
        }

        // final sort by arrival time for display
        results.sort(Comparator.comparingInt(j -> j.arrivalMin));
        return results;
    }

    // ── helpers ───────────────────────────────────────────────────────

    private String journeyKey(Journey j) {
        StringBuilder sb = new StringBuilder();
        for (JourneyLeg l : j.legs) {
            sb.append(l.routeId).append(':')
                    .append(l.boardStopId).append(':')
                    .append(l.alightStopId).append('|');
        }
        return sb.toString();
    }

    private int binarySearchTrip(List<BusTrip> trips, int seq, int earliestDep) {
        int lo = 0, hi = trips.size() - 1, result = -1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (trips.get(mid).stopTimes.get(seq).departureMin >= earliestDep) {
                result = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return result;
    }

    private Journey reconstruct(Label tail, int sourceId, int departureMin) {
        ArrayDeque<JourneyLeg> stack = new ArrayDeque<>();
        Label cur = tail;
        while (cur != null) {
            BusTrip trip = findTrip(cur.getTripId());
            if (trip == null) return null;
            int dep = -1, arr = -1;
            for (StopTime st : trip.stopTimes) {
                if (st.stopId == cur.getBoardStop())  dep = st.departureMin;
                if (st.stopId == cur.getAlightStop()) arr = st.arrivalMin;
            }
            if (dep < 0 || arr < 0) return null;
            stack.push(new JourneyLeg(cur.getRouteId(), cur.getBoardStop(),
                    cur.getAlightStop(), dep, arr));
            cur = cur.getPrev();
        }

        List<JourneyLeg> legs = new ArrayList<>(stack);
        if (legs.isEmpty() || legs.get(0).boardStopId != sourceId) return null;

        int arrival  = legs.get(legs.size() - 1).arrivalMin;
        int duration = arrival - departureMin;
        return new Journey(arrival, duration, legs.size() - 1, legs);
    }

    private BusTrip findTrip(String tripId) {
        for (BusTrip t : tripGen.getAllTrips())
            if (t.tripId.equals(tripId)) return t;
        return null;
    }
}
package com.Bus.Routing.Bus.controller;

import com.Bus.Routing.Bus.dto.ChatRequest;
import com.Bus.Routing.Bus.engine.TransitData;
import com.Bus.Routing.Bus.engine.TripGenerator;
import com.Bus.Routing.Bus.model.BusTrip;
import com.Bus.Routing.Bus.model.Stop;
import com.Bus.Routing.Bus.model.StopTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final TransitData transitData;
    private final TripGenerator tripGenerator;

    public ChatController(TransitData transitData,
                          TripGenerator tripGenerator) {
        this.transitData = transitData;
        this.tripGenerator = tripGenerator;
    }

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create();

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody ChatRequest request) {

        String userMessage = request.message;

        // ================================
        // 1️⃣ Build Full Dataset From Models
        // ================================

        StringBuilder dataset = new StringBuilder();

        dataset.append("BUS STOPS:\n");

        for (Stop stop : transitData.getStops()) {
            dataset.append("- ")
                    .append(stop.name)
                    .append(" (")
                    .append(stop.lat)
                    .append(", ")
                    .append(stop.lon)
                    .append(")\n");
        }

        dataset.append("\nBUS ROUTES & TRIPS:\n");

        for (BusTrip trip : tripGenerator.getAllTrips()) {

            dataset.append("Route: ")
                    .append(trip.routeId)
                    .append(" | Trip: ")
                    .append(trip.tripId)
                    .append("\n");

            for (StopTime st : trip.stopTimes) {

                Stop stop = transitData.getStop(st.stopId);

                dataset.append("   → ")
                        .append(stop.name)
                        .append(" | Arrival: ")
                        .append(formatTime(st.arrivalMin))
                        .append(" | Departure: ")
                        .append(formatTime(st.departureMin))
                        .append("\n");
            }

            dataset.append("\n");
        }

        // ================================
        // 2️⃣ Prompt Engineering (UNCHANGED)
        // ================================

        String prompt = """
You are a friendly and intelligent bus travel assistant.

STRICT RULES:
- Use ONLY the dataset provided below.
- Do NOT invent routes, buses, stops, or timings.
- If no route exists, clearly say so.
- If user asks for bus stops, list them from dataset.
- If user asks for route between two stops:
   • Ensure correct stop order.
   • Calculate travel time correctly.
   • Mention departure and arrival times.
- Speak conversationally like a human travel agent.

Transit Dataset:
""" + dataset + """

User Question:
""" + userMessage;

        String geminiUrl =
                "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key="
                        + apiKey;

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        Map response = webClient.post()
                .uri(geminiUrl)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map candidate = (Map) ((List<?>) response.get("candidates")).get(0);
        Map content = (Map) candidate.get("content");
        List parts = (List<?>) content.get("parts");
        Map textPart = (Map) parts.get(0);

        String reply = textPart.get("text").toString();

        return Map.of("reply", reply);
    }

    private String formatTime(int mins) {
        return String.format("%02d:%02d", mins / 60, mins % 60);
    }
}

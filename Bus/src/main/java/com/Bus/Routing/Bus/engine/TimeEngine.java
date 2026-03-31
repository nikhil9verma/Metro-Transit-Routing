package com.Bus.Routing.Bus.engine;


import org.springframework.stereotype.Component;

@Component
public class TimeEngine {

    public static final int SERVICE_START = 5 * 60 + 15;
    public static final int SERVICE_END   = 23 * 60 + 15;

    public boolean isPeak(int minuteOfDay) {
        return (minuteOfDay >= 7 * 60 && minuteOfDay < 10 * 60)
                || (minuteOfDay >= 17 * 60 && minuteOfDay < 20 * 60);
    }

    public int segmentDuration(String type, boolean isExpress, int departureMin) {
        int base;
        switch (type) {
            case "short" -> base = 5;
            case "long"  -> base = 12;
            default      -> base = 8;
        }
        if (isExpress) base = (int) Math.round(base * 0.75);
        if (isPeak(departureMin)) base = (int) Math.round(base * 1.2);
        return Math.max(base, 3);
    }

    public String formatTime(int totalMinutes) {
        int min = totalMinutes % (24 * 60);
        if (min < 0) min += 24 * 60;
        int h = min / 60;
        int m = min % 60;
        return String.format("%02d:%02d", h, m);
    }

    public int parseTime(String hhmm) {
        String[] parts = hhmm.trim().split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    public int headwayMinutes(int minuteOfDay) {
        return isPeak(minuteOfDay) ? 8 : 15;
    }
}

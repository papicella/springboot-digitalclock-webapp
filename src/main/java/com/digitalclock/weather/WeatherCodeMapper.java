package com.digitalclock.weather;

public final class WeatherCodeMapper {

    private WeatherCodeMapper() {}

    public static String describe(int code) {
        return switch (code) {
            case 0 -> "Clear sky";
            case 1 -> "Mainly clear";
            case 2 -> "Partly cloudy";
            case 3 -> "Overcast";
            case 45, 48 -> "Fog";
            case 51, 53, 55 -> "Drizzle";
            case 56, 57 -> "Freezing drizzle";
            case 61, 63, 65 -> "Rain";
            case 66, 67 -> "Freezing rain";
            case 71, 73, 75 -> "Snow";
            case 77 -> "Snow grains";
            case 80, 81, 82 -> "Rain showers";
            case 85, 86 -> "Snow showers";
            case 95 -> "Thunderstorm";
            case 96, 99 -> "Thunderstorm with hail";
            default -> "Unknown";
        };
    }

    public static String icon(int code) {
        return switch (code) {
            case 0 -> "☀";
            case 1, 2 -> "⛅";
            case 3 -> "☁";
            case 45, 48 -> "🌫";
            case 51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82 -> "🌧";
            case 71, 73, 75, 77, 85, 86 -> "❄";
            case 95, 96, 99 -> "⛈";
            default -> "🌡";
        };
    }
}

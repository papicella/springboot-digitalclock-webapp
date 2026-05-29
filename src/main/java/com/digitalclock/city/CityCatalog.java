package com.digitalclock.city;

import java.util.List;
import java.util.Optional;

public final class CityCatalog {

    public static final String DEFAULT_CITY_ID = "melbourne";

    public static final List<City> CITIES = List.of(
            new City("melbourne", "Melbourne, Australia", "Australia/Melbourne", -37.8136, 144.9631),
            new City("sydney", "Sydney, Australia", "Australia/Sydney", -33.8688, 151.2093),
            new City("honolulu", "Honolulu", "Pacific/Honolulu", 21.3069, -157.8583),
            new City("anchorage", "Anchorage", "America/Anchorage", 61.2181, -149.9003),
            new City("los-angeles", "Los Angeles", "America/Los_Angeles", 34.0522, -118.2437),
            new City("denver", "Denver", "America/Denver", 39.7392, -104.9903),
            new City("chicago", "Chicago", "America/Chicago", 41.8781, -87.6298),
            new City("new-york", "New York", "America/New_York", 40.7128, -74.0060),
            new City("sao-paulo", "São Paulo", "America/Sao_Paulo", -23.5505, -46.6333),
            new City("london", "London", "Europe/London", 51.5074, -0.1278),
            new City("paris", "Paris", "Europe/Paris", 48.8566, 2.3522),
            new City("berlin", "Berlin", "Europe/Berlin", 52.5200, 13.4050),
            new City("athens", "Athens", "Europe/Athens", 37.9838, 23.7275),
            new City("cairo", "Cairo", "Africa/Cairo", 30.0444, 31.2357),
            new City("dubai", "Dubai", "Asia/Dubai", 25.2048, 55.2708),
            new City("new-delhi", "New Delhi", "Asia/Kolkata", 28.6139, 77.2090),
            new City("bangkok", "Bangkok", "Asia/Bangkok", 13.7563, 100.5018),
            new City("singapore", "Singapore", "Asia/Singapore", 1.3521, 103.8198),
            new City("tokyo", "Tokyo", "Asia/Tokyo", 35.6762, 139.6503),
            new City("sydney", "Sydney", "Australia/Sydney", -33.8688, 151.2093),
            new City("auckland", "Auckland", "Pacific/Auckland", -36.8509, 174.7645),
            new City("utc", "Greenwich (UTC)", "UTC", 51.4769, 0.0005),
            // Added 4 more cities
            new City("mumbai", "Mumbai", "Asia/Kolkata", 19.0760, 72.8777),
            new City("moscow", "Moscow", "Europe/Moscow", 55.7558, 37.6173),
            new City("cape-town", "Cape Town", "Africa/Johannesburg", -33.9249, 18.4241),
            new City("vancouver", "Vancouver", "America/Vancouver", 49.2827, -123.1207)
    );

    public static final List<String> TIMEZONE_IDS =
            CITIES.stream().map(City::timezoneId).distinct().toList();

    public static City resolveCity(String cityId) {
        return findById(cityId).orElseGet(CityCatalog::defaultCity);
    }

    public static City defaultCity() {
        return findById(DEFAULT_CITY_ID).orElse(CITIES.get(0));
    }

    public static Optional<City> findById(String cityId) {
        if (cityId == null || cityId.isBlank()) {
            return Optional.empty();
        }
        return CITIES.stream().filter(city -> city.id().equals(cityId)).findFirst();
    }

    private CityCatalog() {}
}

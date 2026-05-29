package com.digitalclock.weather;

public record CityWeatherView(
        String cityName,
        String timezoneId,
        boolean available,
        String icon,
        String condition,
        String temperature,
        String feelsLike,
        String humidity,
        String precipitation,
        String wind,
        String windDirection,
        String cloudCover,
        String pressure,
        String visibility,
        String dayNight,
        String observedAt,
        String errorMessage) {

    public static CityWeatherView unavailable(String cityName, String timezoneId, String errorMessage) {
        return new CityWeatherView(
                cityName,
                timezoneId,
                false,
                "—",
                "Unavailable",
                "—",
                "—",
                "—",
                "—",
                "—",
                "—",
                "—",
                "—",
                "—",
                "—",
                "—",
                errorMessage);
    }
}

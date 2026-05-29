package com.digitalclock.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenMeteoResponse(
        CurrentWeather current,
        @JsonProperty("current_units") CurrentUnits currentUnits) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CurrentWeather(
            String time,
            @JsonProperty("temperature_2m") Double temperature2m,
            @JsonProperty("apparent_temperature") Double apparentTemperature,
            @JsonProperty("relative_humidity_2m") Integer relativeHumidity2m,
            @JsonProperty("precipitation") Double precipitation,
            @JsonProperty("weather_code") Integer weatherCode,
            @JsonProperty("wind_speed_10m") Double windSpeed10m,
            @JsonProperty("wind_direction_10m") Integer windDirection10m,
            @JsonProperty("cloud_cover") Integer cloudCover,
            @JsonProperty("surface_pressure") Double surfacePressure,
            @JsonProperty("visibility") Double visibility,
            @JsonProperty("is_day") Integer isDay) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CurrentUnits(
            @JsonProperty("temperature_2m") String temperature2m,
            @JsonProperty("apparent_temperature") String apparentTemperature,
            @JsonProperty("relative_humidity_2m") String relativeHumidity2m,
            @JsonProperty("precipitation") String precipitation,
            @JsonProperty("wind_speed_10m") String windSpeed10m,
            @JsonProperty("surface_pressure") String surfacePressure,
            @JsonProperty("visibility") String visibility) {}
}

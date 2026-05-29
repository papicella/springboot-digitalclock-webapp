package com.digitalclock.weather;

import com.digitalclock.city.City;
import com.digitalclock.city.CityCatalog;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    private static final String CURRENT_PARAMS =
            "temperature_2m,apparent_temperature,relative_humidity_2m,precipitation,"
                    + "weather_code,wind_speed_10m,wind_direction_10m,cloud_cover,"
                    + "surface_pressure,visibility,is_day";

    private final RestClient restClient;
    private final String forecastUrl;

    public WeatherService(
            RestClient.Builder restClientBuilder,
            @Value("${weather.api.url:https://api.open-meteo.com/v1/forecast}") String forecastUrl) {
        this.restClient = restClientBuilder.build();
        this.forecastUrl = forecastUrl;
    }

    public CityWeatherView fetchCityWeather(String cityId) {
        return fetchCityWeather(CityCatalog.resolveCity(cityId));
    }

    private CityWeatherView fetchCityWeather(City city) {
        try {
            String uri = UriComponentsBuilder.fromHttpUrl(forecastUrl)
                    .queryParam("latitude", city.latitude())
                    .queryParam("longitude", city.longitude())
                    .queryParam("current", CURRENT_PARAMS)
                    .queryParam("timezone", city.timezoneId())
                    .queryParam("wind_speed_unit", "kmh")
                    .toUriString();

            OpenMeteoResponse response =
                    restClient.get().uri(uri).retrieve().body(OpenMeteoResponse.class);

            if (response == null || response.current() == null) {
                return CityWeatherView.unavailable(
                        city.displayName(), city.timezoneId(), "No weather data returned");
            }

            return toView(city, response);
        } catch (RestClientException ex) {
            return CityWeatherView.unavailable(
                    city.displayName(), city.timezoneId(), "Could not load weather data");
        }
    }

    private static CityWeatherView toView(City city, OpenMeteoResponse response) {
        OpenMeteoResponse.CurrentWeather current = response.current();
        OpenMeteoResponse.CurrentUnits units = response.currentUnits();

        String tempUnit = unit(units != null ? units.temperature2m() : null, "°C");
        String feelsUnit = unit(units != null ? units.apparentTemperature() : null, "°C");
        String humidityUnit = unit(units != null ? units.relativeHumidity2m() : null, "%");
        String precipUnit = unit(units != null ? units.precipitation() : null, "mm");
        String windUnit = unit(units != null ? units.windSpeed10m() : null, "km/h");
        String pressureUnit = unit(units != null ? units.surfacePressure() : null, "hPa");
        String visibilityUnit = unit(units != null ? units.visibility() : null, "m");

        int weatherCode = current.weatherCode() != null ? current.weatherCode() : -1;
        boolean isDay = current.isDay() != null && current.isDay() == 1;

        return new CityWeatherView(
                city.displayName(),
                city.timezoneId(),
                true,
                WeatherCodeMapper.icon(weatherCode),
                WeatherCodeMapper.describe(weatherCode),
                formatValue(current.temperature2m(), tempUnit),
                formatValue(current.apparentTemperature(), feelsUnit),
                formatValue(current.relativeHumidity2m(), humidityUnit),
                formatValue(current.precipitation(), precipUnit),
                formatValue(current.windSpeed10m(), windUnit),
                formatWindDirection(current.windDirection10m()),
                formatValue(current.cloudCover(), "%"),
                formatValue(current.surfacePressure(), pressureUnit),
                formatVisibility(current.visibility(), visibilityUnit),
                isDay ? "Day" : "Night",
                current.time() != null ? current.time() : "—",
                null);
    }

    private static String unit(String fromApi, String fallback) {
        return fromApi != null && !fromApi.isBlank() ? fromApi : fallback;
    }

    private static String formatValue(Number value, String unit) {
        if (value == null) {
            return "—";
        }
        if (value instanceof Double doubleValue) {
            return String.format("%.1f%s", doubleValue, unit);
        }
        return value + unit;
    }

    private static String formatVisibility(Double meters, String unit) {
        if (meters == null) {
            return "—";
        }
        if (meters >= 1000) {
            return String.format("%.1f km", meters / 1000.0);
        }
        return String.format("%.0f%s", meters, unit);
    }

    private static String formatWindDirection(Integer degrees) {
        if (degrees == null) {
            return "—";
        }
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        int index = (int) Math.round(degrees / 45.0) % 8;
        return directions[index] + " (" + degrees + "°)";
    }
}

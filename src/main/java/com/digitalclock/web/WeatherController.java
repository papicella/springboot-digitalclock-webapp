package com.digitalclock.web;

import com.digitalclock.city.CityCatalog;
import com.digitalclock.weather.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String weather(
            @RequestParam(value = "city", defaultValue = CityCatalog.DEFAULT_CITY_ID) String cityId,
            Model model) {
        String selectedCityId = CityCatalog.findById(cityId).map(c -> c.id()).orElse(CityCatalog.DEFAULT_CITY_ID);

        model.addAttribute("weather", weatherService.fetchCityWeather(selectedCityId));
        model.addAttribute("cityOptions", CityCatalog.CITIES);
        model.addAttribute("selectedCityId", selectedCityId);
        return "weather";
    }
}

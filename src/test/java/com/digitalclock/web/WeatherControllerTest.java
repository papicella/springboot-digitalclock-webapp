package com.digitalclock.web;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.digitalclock.city.CityCatalog;
import com.digitalclock.weather.CityWeatherView;
import com.digitalclock.weather.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    void weatherDefaultsToMelbourne() throws Exception {
        when(weatherService.fetchCityWeather(eq(CityCatalog.DEFAULT_CITY_ID)))
                .thenReturn(CityWeatherView.unavailable(
                        "Melbourne, Australia", "Australia/Melbourne", "test"));

        mockMvc.perform(get("/weather"))
                .andExpect(status().isOk())
                .andExpect(view().name("weather"))
                .andExpect(model().attributeExists("weather"))
                .andExpect(model().attribute("selectedCityId", CityCatalog.DEFAULT_CITY_ID));
    }

    @Test
    void weatherAcceptsCityQueryParam() throws Exception {
        when(weatherService.fetchCityWeather(eq("london")))
                .thenReturn(CityWeatherView.unavailable("London", "Europe/London", "test"));

        mockMvc.perform(get("/weather").param("city", "london"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedCityId", "london"));
    }
}

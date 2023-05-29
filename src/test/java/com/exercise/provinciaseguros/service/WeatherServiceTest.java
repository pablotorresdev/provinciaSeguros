package com.exercise.provinciaseguros.service;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.exercise.provinciaseguros.external.accuweather.AccuWeatherClient;
import com.exercise.provinciaseguros.model.WeatherEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WeatherServiceTest {

    private WeatherService weatherService;

    @Mock
    private AccuWeatherClient accuWeatherClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        weatherService = new WeatherService();
        ReflectionTestUtils.setField(weatherService, "accuWeatherClient", accuWeatherClient);
    }

    @Test
    void testGetCurrentWeather() {
        String location = "LOCATION_KEY";
        WeatherEntity weatherEntity = new WeatherEntity();
        when(accuWeatherClient.getCurrentWeather(location)).thenReturn(Optional.of(weatherEntity));

        Optional<WeatherEntity> result = weatherService.getCurrentWeather(location);

        assertEquals(Optional.of(weatherEntity), result);
        verify(accuWeatherClient, times(1)).getCurrentWeather(location);
    }

}
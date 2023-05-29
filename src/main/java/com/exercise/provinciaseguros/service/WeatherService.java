package com.exercise.provinciaseguros.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.provinciaseguros.external.accuweather.AccuWeatherClient;
import com.exercise.provinciaseguros.model.WeatherEntity;

@Service
public class WeatherService {

    @Autowired
    private AccuWeatherClient accuWeatherClient;

    /**
     * Get current weather for a location
     *
     * @param location  Location to get weather information
     * @return Optional with a WeatherEntity object if weather information is available,
     * or an empty Optional if no weather information is available.
     */
    public Optional<WeatherEntity> getCurrentWeather(String location) {
        return accuWeatherClient.getCurrentWeather(location);
    }

}

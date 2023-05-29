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

    public Optional<WeatherEntity> getCurrentWeather(String location) {
        return accuWeatherClient.getCurrentWeather(location);
    }

}

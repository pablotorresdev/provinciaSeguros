package com.exercise.provinciaseguros.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exercise.provinciaseguros.external.accuweather.AccuWeatherClient;
import com.exercise.provinciaseguros.model.CurrentWeather;

@Service
public class WeatherService {

    @Autowired
    private AccuWeatherClient accuWeatherClient;

    public CurrentWeather getCurrentWeather(String location) {
        // Make API call to AccuWeather to retrieve current weather data for the specified location
        return accuWeatherClient.getCurrentWeather(location);
    }

}

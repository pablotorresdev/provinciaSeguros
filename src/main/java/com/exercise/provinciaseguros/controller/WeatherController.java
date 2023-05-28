package com.exercise.provinciaseguros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.provinciaseguros.model.CurrentWeather;
import com.exercise.provinciaseguros.service.WeatherService;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{location}")
    public ResponseEntity<CurrentWeather> getCurrentWeather(@PathVariable String location) {
        CurrentWeather weatherResponse = weatherService.getCurrentWeather(location);
        if (weatherResponse != null) {
            return ResponseEntity.ok(weatherResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
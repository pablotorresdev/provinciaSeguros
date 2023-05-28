package com.exercise.provinciaseguros.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {

    private String location;
    private double temperature;
    private double humidity;
    private double windSpeed;
    private String weatherConditions;

}

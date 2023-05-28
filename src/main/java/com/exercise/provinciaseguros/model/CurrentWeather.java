package com.exercise.provinciaseguros.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentWeather {
    private String weatherText;
    private boolean hasPrecipitation;
    private String precipitationType;
    private double temperature;
}



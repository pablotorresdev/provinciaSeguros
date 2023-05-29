package com.exercise.provinciaseguros.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {

    private String location;

    private String localObservationDateTime;

    private String weatherText;

    private boolean hasPrecipitation;

    private String precipitationType;

    private double temperature;

}



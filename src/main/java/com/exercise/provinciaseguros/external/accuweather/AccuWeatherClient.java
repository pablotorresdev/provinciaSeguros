package com.exercise.provinciaseguros.external.accuweather;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.exercise.provinciaseguros.model.WeatherEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AccuWeatherClient {

    @Value("${accuweather.api.key}")
    private String apiKey;

    @Value("${accuweather.base.url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public AccuWeatherClient() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public Optional<WeatherEntity> getCurrentWeather(String location) {
        String url = baseUrl + "/currentconditions/v1/{locationKey}";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
            .queryParam("apikey", apiKey).queryParam("metric", true);

        ResponseEntity<String> response = restTemplate.exchange(builder.buildAndExpand(location).toUri(),
            HttpMethod.GET,
            null,
            String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            try {
                JsonNode root = objectMapper.readTree(responseBody);
                if (root.isArray() && root.size() > 0) {
                    JsonNode weatherNode = root.get(0);
                    return Optional.of(convertWeatherNodeToCurrentWeather(weatherNode, location));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    WeatherEntity convertWeatherNodeToCurrentWeather(final JsonNode weatherNode, String location) {
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setLocalObservationDateTime(weatherNode.path("LocalObservationDateTime").asText());
        weatherEntity.setLocation(location);

        JsonNode temperatureNode = weatherNode.path("Temperature").path("Metric");
        if (!temperatureNode.isMissingNode()) {
            weatherEntity.setTemperature(temperatureNode.path("Value").asDouble());
        }

        weatherEntity.setWeatherText(weatherNode.path("WeatherText").asText());
        boolean hasPrecipitation = weatherNode.path("HasPrecipitation").asBoolean();
        weatherEntity.setHasPrecipitation(hasPrecipitation);
        weatherEntity.setPrecipitationType(hasPrecipitation ? weatherNode.path("PrecipitationType").asText() : "None");

        return weatherEntity;
    }

}


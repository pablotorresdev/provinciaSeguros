package com.exercise.provinciaseguros.external.accuweather;

import java.net.URI;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.exercise.provinciaseguros.model.WeatherEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

class AccuWeatherClientTest {

    private AccuWeatherClient accuWeatherClient;

    @Value("${accuweather.api.key}")
    private String apiKey;

    @Value("${accuweather.base.url}")
    private String baseUrl;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        accuWeatherClient = new AccuWeatherClient();
        ReflectionTestUtils.setField(accuWeatherClient, "restTemplate", restTemplate);
    }

    @Test
    void testGetCurrentWeather_SuccessfulResponse() {
        String responseBody = "[{\"LocalObservationDateTime\":\"2022-01-01T09:00:00\",\"Temperature\":{\"Metric\":{\"Value\":20.5}},\"WeatherText\":\"Sunny\",\"HasPrecipitation\":false}]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        doReturn(responseEntity).when(restTemplate).exchange(any(URI.class), eq(HttpMethod.GET), eq(null), eq(String.class));

        Optional<WeatherEntity> result = accuWeatherClient.getCurrentWeather("LOCATION_KEY");

        assertTrue(result.isPresent());
        WeatherEntity weatherEntity = result.get();
        assertEquals("2022-01-01T09:00:00", weatherEntity.getLocalObservationDateTime());
        assertEquals("LOCATION_KEY", weatherEntity.getLocation());
        assertEquals(20.5, weatherEntity.getTemperature());
        assertEquals("Sunny", weatherEntity.getWeatherText());
        assertFalse(weatherEntity.isHasPrecipitation());
        assertEquals("None", weatherEntity.getPrecipitationType());
    }

    @Test
    void testGetCurrentWeather_EmptyResponse() {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        doReturn(responseEntity).when(restTemplate).exchange(any(URI.class), eq(HttpMethod.GET), eq(null), eq(String.class));

        Optional<WeatherEntity> result = accuWeatherClient.getCurrentWeather("LOCATION_KEY");

        assertFalse(result.isPresent());
    }

    @Test
    void testGetCurrentWeather_Exception() {
        doThrow(new RuntimeException("Internal server error")).when(restTemplate)
            .exchange(any(URI.class), eq(HttpMethod.GET), eq(null), eq(String.class));

        assertThrows(RuntimeException.class, () -> {
            accuWeatherClient.getCurrentWeather("LOCATION_KEY");
        });
    }

    @Test
    void testConvertWeatherNodeToCurrentWeatherNoPrecipitation() {
        // Create a sample weatherNode
        JsonNode weatherNode = createSampleWeatherNode(false);

        // Set the location
        String location = "LOCATION";

        // Invoke the method
        WeatherEntity weatherEntity = accuWeatherClient.convertWeatherNodeToCurrentWeather(weatherNode, location);

        // Perform assertions
        assertEquals(weatherNode.path("LocalObservationDateTime").asText(), weatherEntity.getLocalObservationDateTime());
        assertEquals(location, weatherEntity.getLocation());
        assertEquals(weatherNode.path("Temperature").path("Metric").path("Value").asDouble(), weatherEntity.getTemperature());
        assertEquals(weatherNode.path("WeatherText").asText(), weatherEntity.getWeatherText());
        assertEquals(weatherNode.path("HasPrecipitation").asBoolean(), weatherEntity.isHasPrecipitation());
        assertEquals("None", weatherEntity.getPrecipitationType());
    }

    private JsonNode createSampleWeatherNode(boolean hasPrecipitation) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode weatherNode = objectMapper.createObjectNode();
        ObjectNode temperatureNode = objectMapper.createObjectNode();
        ObjectNode metricNode = objectMapper.createObjectNode();

        metricNode.put("Value", 20.5);
        temperatureNode.set("Metric", metricNode);
        weatherNode.set("Temperature", temperatureNode);

        // Add other properties to the weatherNode
        weatherNode.put("LocalObservationDateTime", "2022-01-01T09:00:00");
        weatherNode.put("WeatherText", "Cloudy");
        weatherNode.put("HasPrecipitation", hasPrecipitation);
        weatherNode.put("PrecipitationType", "Rain");

        return weatherNode;
    }

    @Test
    void testConvertWeatherNodeToCurrentWeatherHasPrecipitation() {
        // Create a sample weatherNode
        JsonNode weatherNode = createSampleWeatherNode(true);

        // Set the location
        String location = "LOCATION";

        // Invoke the method
        WeatherEntity weatherEntity = accuWeatherClient.convertWeatherNodeToCurrentWeather(weatherNode, location);

        // Perform assertions
        assertEquals(weatherNode.path("LocalObservationDateTime").asText(), weatherEntity.getLocalObservationDateTime());
        assertEquals(location, weatherEntity.getLocation());
        assertEquals(weatherNode.path("Temperature").path("Metric").path("Value").asDouble(), weatherEntity.getTemperature());
        assertEquals(weatherNode.path("WeatherText").asText(), weatherEntity.getWeatherText());
        assertEquals(weatherNode.path("HasPrecipitation").asBoolean(), weatherEntity.isHasPrecipitation());
        assertEquals(weatherNode.path("PrecipitationType").asText(), weatherEntity.getPrecipitationType());
    }

}
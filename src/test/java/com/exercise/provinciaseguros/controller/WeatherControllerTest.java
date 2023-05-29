package com.exercise.provinciaseguros.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.exercise.provinciaseguros.model.WeatherEntity;
import com.exercise.provinciaseguros.model.WeatherResponse;
import com.exercise.provinciaseguros.repository.WeatherRepository;
import com.exercise.provinciaseguros.service.WeatherService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WeatherControllerTest {

    private WeatherController weatherController;

    @Mock
    private WeatherService weatherService;

    @Mock
    private WeatherRepository weatherRepository;

    private static final String LOCATION = "LOCATION";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        weatherController = new WeatherController();
        ReflectionTestUtils.setField(weatherController, "weatherService", weatherService);
        ReflectionTestUtils.setField(weatherController, "weatherRepository", weatherRepository);
        ReflectionTestUtils.setField(weatherController, "intervalSeconds", 60);
    }

    @Test
    void testGetAllLocations_WithWeatherEntities_ReturnsOkResponse() {
        List<WeatherEntity> weatherEntities = new ArrayList<>();
        weatherEntities.add(createEntity());
        when(weatherRepository.findAll()).thenReturn(weatherEntities);

        ResponseEntity<List<WeatherResponse>> response = weatherController.getAllLocations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(weatherRepository, times(1)).findAll();
    }

    private static WeatherEntity createEntity() {
        WeatherEntity entity = new WeatherEntity();
        entity.setTemperature(11.3);
        entity.setLocation(LOCATION);
        entity.setWeatherText("WEATHER_TEXT");
        entity.setLocalObservationDateTime("LOCAL_OBSERVATION_DATE_TIME");
        entity.setPrecipitationType("PRECIPITATION_TYPE");
        entity.setHasPrecipitation(true);
        return entity;
    }

    @Test
    void testGetAllLocations_WithNoWeatherEntities_ReturnsNoContentResponse() {
        when(weatherRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<List<WeatherResponse>> response = weatherController.getAllLocations();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(weatherRepository, times(1)).findAll();
    }

    @Test
    void testGetAllWeatherForLocation_WithWeatherEntities_ReturnsOkResponse() {
        List<WeatherEntity> weatherEntities = new ArrayList<>();
        weatherEntities.add(createEntity());
        when(weatherRepository.findByLocation(LOCATION)).thenReturn(weatherEntities);

        ResponseEntity<List<WeatherResponse>> response = weatherController.getAllWeatherForLocation(LOCATION);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(weatherRepository, times(1)).findByLocation(LOCATION);
        verify(weatherService, times(0)).getCurrentWeather(LOCATION);
    }

    @Test
    void testGetAllWeatherForLocation_WithNoWeatherEntities_ReturnsNotFoundResponse() {
        when(weatherRepository.findByLocation(LOCATION)).thenReturn(new ArrayList<>());

        ResponseEntity<List<WeatherResponse>> response = weatherController.getAllWeatherForLocation(LOCATION);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(weatherRepository, times(1)).findByLocation(LOCATION);
        verify(weatherService, times(0)).getCurrentWeather(LOCATION);
    }

    @Test
    void testGetCurrentWeather_WithExistingWeatherEntity_ReturnsOkResponse() {

        WeatherEntity weatherEntity = createEntity();
        List<WeatherEntity> resultList = new ArrayList<>();
        resultList.add(weatherEntity);

        when(weatherRepository.findByLocationWithTimestampedAtAfter(eq(LOCATION), any(LocalDateTime.class))).thenReturn(resultList);

        ResponseEntity<WeatherResponse> response = weatherController.getCurrentWeather(LOCATION);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(weatherController.convertToWeatherResponse(weatherEntity), response.getBody());
        verify(weatherRepository, times(1)).findByLocationWithTimestampedAtAfter(eq(LOCATION), any(LocalDateTime.class));
    }

    @Test
    void testGetCurrentWeather_WithNewWeatherEntity_ReturnsOkResponse() {
        WeatherEntity weatherEntity = createEntity();
        Optional<WeatherEntity> optApiWeatherEntity = Optional.of(weatherEntity);
        when(weatherService.getCurrentWeather(LOCATION)).thenReturn(optApiWeatherEntity);
        when(weatherRepository.findByLocationAndLocalObservationDateTime(
            eq(LOCATION),
            eq(weatherEntity.getLocalObservationDateTime()))).thenReturn(Optional.empty());
        when(weatherRepository.save(weatherEntity)).thenReturn(weatherEntity);

        ResponseEntity<WeatherResponse> response = weatherController.getCurrentWeather(LOCATION);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(weatherController.convertToWeatherResponse(weatherEntity), response.getBody());
        verify(weatherRepository, times(1)).findByLocationWithTimestampedAtAfter(eq(LOCATION), any(LocalDateTime.class));
        verify(weatherService, times(1)).getCurrentWeather(LOCATION);
        verify(weatherRepository, times(1)).findByLocationAndLocalObservationDateTime(
            eq(LOCATION),
            eq(weatherEntity.getLocalObservationDateTime()));
        verify(weatherRepository, times(1)).save(weatherEntity);
    }

    @Test
    void testGetCurrentWeather_WithNoWeatherEntity_ReturnsNotFoundResponse() {

        when(weatherRepository.findByLocationWithTimestampedAtAfter(eq(LOCATION), any(LocalDateTime.class))).thenReturn(new ArrayList<>());
        when(weatherService.getCurrentWeather(LOCATION)).thenReturn(Optional.empty());

        ResponseEntity<WeatherResponse> response = weatherController.getCurrentWeather(LOCATION);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(weatherRepository, times(1)).findByLocationWithTimestampedAtAfter(eq(LOCATION), any(LocalDateTime.class));
        verify(weatherRepository, never()).findByLocationAndLocalObservationDateTime(anyString(), anyString());
        verify(weatherRepository, never()).save(any(WeatherEntity.class));
        verify(weatherService, times(1)).getCurrentWeather(LOCATION);
    }

    @Test
    void testConvertToWeatherResponse() {
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setLocation("New York");
        weatherEntity.setWeatherText("Sunny");
        weatherEntity.setLocalObservationDateTime("2023-05-26T12:00:00");
        weatherEntity.setHasPrecipitation(false);
        weatherEntity.setPrecipitationType("None");
        weatherEntity.setTemperature(25.5);

        WeatherResponse weatherResponse = weatherController.convertToWeatherResponse(weatherEntity);

        assertEquals(weatherEntity.getLocation(), weatherResponse.getLocation());
        assertEquals(weatherEntity.getWeatherText(), weatherResponse.getWeatherText());
        assertEquals(weatherEntity.getLocalObservationDateTime(), weatherResponse.getLocalObservationDateTime());
        assertEquals(weatherEntity.isHasPrecipitation(), weatherResponse.isHasPrecipitation());
        assertEquals(weatherEntity.getPrecipitationType(), weatherResponse.getPrecipitationType());
        assertEquals(weatherEntity.getTemperature(), weatherResponse.getTemperature());
    }

}
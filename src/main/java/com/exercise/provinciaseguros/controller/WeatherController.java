package com.exercise.provinciaseguros.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exercise.provinciaseguros.model.WeatherEntity;
import com.exercise.provinciaseguros.model.WeatherResponse;
import com.exercise.provinciaseguros.repository.WeatherRepository;
import com.exercise.provinciaseguros.service.WeatherService;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherRepository weatherRepository;

    /**
     * Interval in seconds to update weather information
     */
    @Value("${controller.interval.seconds}")
    private int intervalSeconds;

    /**
     * Get all locations with current weather
     *
     @return ResponseEntity with a list of WeatherResponse objects in JSON format.
     If weather information is available, returns HTTP status 200 (OK) with the weather information.
     If no weather information is available, returns HTTP status 204 (No Content).
     */
    @GetMapping("/")
    public ResponseEntity<List<WeatherResponse>> getAllLocations() {
        List<WeatherEntity> weatherEntities = weatherRepository.findAll();
        if (!weatherEntities.isEmpty()) {
            List<WeatherResponse> currentWeatherList = weatherEntities.stream()
                .map(this::convertToWeatherResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(currentWeatherList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    WeatherResponse convertToWeatherResponse(WeatherEntity weatherEntity) {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setLocation(weatherEntity.getLocation());
        weatherResponse.setWeatherText(weatherEntity.getWeatherText());
        weatherResponse.setLocalObservationDateTime(weatherEntity.getLocalObservationDateTime());
        weatherResponse.setHasPrecipitation(weatherEntity.isHasPrecipitation());
        weatherResponse.setPrecipitationType(weatherEntity.getPrecipitationType());
        weatherResponse.setTemperature(weatherEntity.getTemperature());
        return weatherResponse;
    }

    /**
     * Get all weather information for a given location
     *
     * @param location the location to get weather information for
     * @return ResponseEntity with a list of WeatherResponse objects in JSON format.
     * If weather information is available, returns HTTP status 200 (OK) with the weather information.
     * If no weather information is available, returns HTTP status 404 (Not Found).
     */
    @GetMapping("/{location}")
    public ResponseEntity<List<WeatherResponse>> getAllWeatherForLocation(@PathVariable String location) {
        List<WeatherEntity> weatherEntities = weatherRepository.findByLocation(location);
        if (!weatherEntities.isEmpty()) {
            List<WeatherResponse> currentWeatherList = weatherEntities.stream()
                .map(this::convertToWeatherResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(currentWeatherList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get current weather information for a given location
     * @param location the location to get weather information for
     * @return ResponseEntity with a WeatherResponse object in JSON format.
     * If weather information is available, returns HTTP status 200 (OK) with the weather information.
     * If no weather information is available, returns HTTP status 404 (Not Found).
     */
    @GetMapping("/current/{location}")
    public ResponseEntity<WeatherResponse> getCurrentWeather(@PathVariable String location) {
        List<WeatherEntity> findByLocationAndTimestampedAtList = weatherRepository.findByLocationWithTimestampedAtAfter(
            location,
            LocalDateTime.now().minusSeconds(intervalSeconds));
        if (!findByLocationAndTimestampedAtList.isEmpty()) {
            return ResponseEntity.ok(convertToWeatherResponse(findByLocationAndTimestampedAtList.get(0)));
        } else {
            Optional<WeatherEntity> optApiWeatherEntity = weatherService.getCurrentWeather(location);
            if (optApiWeatherEntity.isPresent()) {
                WeatherEntity savedWeatherEntity = upsertEntity(location, optApiWeatherEntity.get());
                return ResponseEntity.ok(convertToWeatherResponse(savedWeatherEntity));
            }
        }
        return ResponseEntity.notFound().build();
    }

    private WeatherEntity upsertEntity(final String location, final WeatherEntity apiWeatherEntity) {
        String time = apiWeatherEntity.getLocalObservationDateTime();

        // Preserve the existing ID for update
        weatherRepository.findByLocationAndLocalObservationDateTime(location, time)
            .ifPresent(existingWeatherEntity -> apiWeatherEntity.setId(existingWeatherEntity.getId()));

        return weatherRepository.save(apiWeatherEntity);
    }

}
package com.exercise.provinciaseguros.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.exercise.provinciaseguros.model.WeatherEntity;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {

    List<WeatherEntity> findByLocation(String location);

    Optional<WeatherEntity> findByLocationAndLocalObservationDateTime(String location, String localObservationDateTime);

    @Query(value = "SELECT w FROM WeatherEntity w " +
        "WHERE w.location = :location AND w.timestampedAt >= :timestamp ")
    List<WeatherEntity> findByLocationWithTimestampedAtAfter(String location, LocalDateTime timestamp);

}

package com.exercise.provinciaseguros.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "weather", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"location", "localObservationDateTime"})
})
public class WeatherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final LocalDateTime timestampedAt;

    private String location;

    private String localObservationDateTime;

    private Double temperature;

    private String weatherText;

    private boolean hasPrecipitation;

    private String precipitationType;

    public WeatherEntity() {
        this.timestampedAt = LocalDateTime.now();
    }

    public WeatherEntity(LocalDateTime timestampedAt) {
        this.timestampedAt = timestampedAt;
    }

}


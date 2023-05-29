package com.exercise.provinciaseguros.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.exercise.provinciaseguros.model.WeatherEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig
@DataJpaTest
class WeatherRepositoryTest {

    @Autowired
    private WeatherRepository weatherRepository;

    @Test
    void testFindAll() {
        WeatherEntity weatherEntity1 = new WeatherEntity();
        weatherEntity1.setLocation("2495");
        weatherRepository.save(weatherEntity1);

        WeatherEntity weatherEntity2 = new WeatherEntity();
        weatherEntity2.setLocation("3595");
        weatherRepository.save(weatherEntity2);

        WeatherEntity weatherEntity3 = new WeatherEntity();
        weatherEntity3.setLocation("8452");
        weatherRepository.save(weatherEntity3);

        List<WeatherEntity> result = weatherRepository.findAll();

        assertEquals(3, result.size());
    }

    @Test
    void testFindByLocation() {
        WeatherEntity weatherEntity1 = new WeatherEntity();
        weatherEntity1.setLocation("8499");
        weatherRepository.save(weatherEntity1);

        WeatherEntity weatherEntity2 = new WeatherEntity();
        weatherEntity2.setLocation("8499");
        weatherRepository.save(weatherEntity2);

        WeatherEntity weatherEntity3 = new WeatherEntity();
        weatherEntity3.setLocation("8798");
        weatherRepository.save(weatherEntity3);

        List<WeatherEntity> result = weatherRepository.findByLocation("8499");

        assertEquals(2, result.size());
        assertEquals("8499", result.get(0).getLocation());
        assertEquals("8499", result.get(1).getLocation());
    }

    @Test
    void testFindByLocation_NoResults() {
        List<WeatherEntity> result = weatherRepository.findByLocation("7667");
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByLocationAndLocalObservationDateTime() {
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setLocation("3212");
        weatherEntity.setLocalObservationDateTime("2023-05-28T16:10:00-03:00");
        weatherRepository.save(weatherEntity);

        Optional<WeatherEntity> result = weatherRepository.findByLocationAndLocalObservationDateTime("3212", "2023-05-28T16:10:00-03:00");

        assertTrue(result.isPresent());
        assertEquals("3212", result.get().getLocation());
        assertEquals("2023-05-28T16:10:00-03:00", result.get().getLocalObservationDateTime());
    }

    @Test
    void testFindByLocationAndLocalObservationDateTime_NoResults() {
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setLocation("6676");
        weatherEntity.setLocalObservationDateTime("2023-05-28T16:10:00-03:00");
        weatherRepository.save(weatherEntity);

        Optional<WeatherEntity> result = weatherRepository.findByLocationAndLocalObservationDateTime("6676", "2023-05-28T17:10:00-03:00");

        assertFalse(result.isPresent());
    }

    @Test
    void testFindByLocationAndCreatedAtBefore() {
        WeatherEntity weatherEntity1 = new WeatherEntity(LocalDateTime.parse("2022-01-01T09:00:00"));
        weatherEntity1.setLocation("1234");
        weatherRepository.save(weatherEntity1);

        WeatherEntity weatherEntity2 = new WeatherEntity(LocalDateTime.parse("2022-01-01T10:00:00"));
        weatherEntity2.setLocation("1234");
        weatherRepository.save(weatherEntity2);

        WeatherEntity weatherEntity3 = new WeatherEntity(LocalDateTime.parse("2022-01-01T11:00:00"));
        weatherEntity3.setLocation("1234");
        weatherRepository.save(weatherEntity3);

        LocalDateTime timestamp = LocalDateTime.parse("2022-01-01T10:30:00");
        List<WeatherEntity> result = weatherRepository.findByLocationWithTimestampedAtAfter("1234", timestamp);

        assertEquals(3, weatherRepository.findAll().size());

        assertEquals(1, result.size());
        assertEquals("1234", result.get(0).getLocation());
        assertEquals(LocalDateTime.parse("2022-01-01T11:00:00"), result.get(0).getTimestampedAt());
    }

    @Test
    void testFindByLocationAndCreatedAtBeforeSingleResult() {
        WeatherEntity weatherEntity1 = new WeatherEntity(LocalDateTime.parse("2022-01-01T09:00:00"));
        weatherEntity1.setLocation("2356");
        weatherRepository.save(weatherEntity1);

        LocalDateTime timestamp = LocalDateTime.parse("2022-01-01T08:30:00");
        List<WeatherEntity> result = weatherRepository.findByLocationWithTimestampedAtAfter("2356", timestamp);

        assertEquals(1, result.size());
        assertEquals("2356", result.get(0).getLocation());
        assertEquals(LocalDateTime.parse("2022-01-01T09:00:00"), result.get(0).getTimestampedAt());

    }

    @Test
    void testFindByLocationAndCreatedAtBefore_NoResults() {
        WeatherEntity weatherEntity1 = new WeatherEntity(LocalDateTime.parse("2022-01-01T09:00:00"));
        weatherEntity1.setLocation("8425");
        weatherRepository.save(weatherEntity1);

        LocalDateTime timestamp = LocalDateTime.parse("2022-01-01T09:30:00");
        List<WeatherEntity> result = weatherRepository.findByLocationWithTimestampedAtAfter("8425", timestamp);

        assertTrue(result.isEmpty());
    }

}
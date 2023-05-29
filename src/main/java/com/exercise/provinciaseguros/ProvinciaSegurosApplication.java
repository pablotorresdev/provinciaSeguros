package com.exercise.provinciaseguros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.exercise.provinciaseguros.model")
public class ProvinciaSegurosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProvinciaSegurosApplication.class, args);
	}

}

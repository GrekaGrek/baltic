package com.air.baltic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BalticApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalticApplication.class, args);
	}

}

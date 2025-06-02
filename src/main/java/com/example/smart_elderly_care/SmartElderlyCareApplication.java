package com.example.smart_elderly_care;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartElderlyCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartElderlyCareApplication.class, args);
	}

}

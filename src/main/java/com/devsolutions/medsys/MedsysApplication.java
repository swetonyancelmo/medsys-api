package com.devsolutions.medsys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MedsysApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedsysApplication.class, args);
	}

}

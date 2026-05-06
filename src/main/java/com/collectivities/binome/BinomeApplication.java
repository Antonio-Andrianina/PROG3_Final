package com.collectivities.binome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BinomeApplication {
	public static void main(String[] args) {
		SpringApplication.run(BinomeApplication.class, args);
	}
}
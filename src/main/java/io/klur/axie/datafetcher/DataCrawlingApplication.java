package io.klur.axie.datafetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DataCrawlingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataCrawlingApplication.class, args);
	}

}

package io.klur.axie.datafetcher;

import io.klur.axie.datafetcher.axie.SoldHistory;
import io.klur.axie.datafetcher.axie.SoldHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class DataCrawlingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataCrawlingApplication.class, args);
	}

	@Bean
	public CommandLineRunner checkDB(SoldHistoryRepository repository) {
		return args -> {
			log.info("check SoldHistory");
			log.info("total : {}", repository.findAll().count().block());
			repository.findAll().doOnEach(e -> log.info(e.toString()));

			repository.save(SoldHistory.builder()
							.id("asdasd")
							.clazz("clazz")
							.name("test")
							.breedCount(1).build());
		};
	}

//	@Bean
//	ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
//		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
//		initializer.setConnectionFactory(connectionFactory);
//		initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
//		return initializer;
//	}

}

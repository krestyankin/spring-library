package ru.krestyankin.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
@EnableHystrix
@EnableCircuitBreaker
public class LibraryApplication {
		public static void main(String[] args) throws Exception {
			ApplicationContext context = SpringApplication.run(LibraryApplication.class, args);
	}

}

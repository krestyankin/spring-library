package ru.krestyankin.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class LibraryApplication {
		public static void main(String[] args) throws Exception {
			ApplicationContext context = SpringApplication.run(LibraryApplication.class, args);
	}

}

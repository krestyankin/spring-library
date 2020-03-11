package ru.krestyankin.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = SpringApplication.run(LibraryApplication.class, args);
	}

}

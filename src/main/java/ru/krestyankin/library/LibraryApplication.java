package ru.krestyankin.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class LibraryApplication {

	public static void main(String[] args) throws Exception {
		//Console.main(args);
		ApplicationContext context = SpringApplication.run(LibraryApplication.class, args);
		//BookDao dao = context.getBean(BookDao.class);
		//System.out.println(dao.getById(3));
	}

}

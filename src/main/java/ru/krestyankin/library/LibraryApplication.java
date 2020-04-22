package ru.krestyankin.library;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.acls.jdbc.JdbcAclService;

@SpringBootApplication
@EnableMongoRepositories
public class LibraryApplication {
		public static void main(String[] args) throws Exception {
			ApplicationContext context = SpringApplication.run(LibraryApplication.class, args);
			Log log = LogFactory.getLog(JdbcAclService.class);
	}

}

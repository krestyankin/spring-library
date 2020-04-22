package ru.krestyankin.library.config;

import com.github.cloudyrock.mongock.SpringBootMongock;
import com.github.cloudyrock.mongock.SpringBootMongockBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ApplicationConfig {
    private static final String CHANGELOGS_PACKAGE = "ru.krestyankin.library.changelogs";

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @ConditionalOnProperty(
            value="library.init",
            matchIfMissing = false
    )
    @Bean
    public SpringBootMongock mongock(MongoTemplate mongoTemplate, ApplicationContext springContext) {
        return new SpringBootMongockBuilder(mongoTemplate, CHANGELOGS_PACKAGE)
                .setApplicationContext(springContext)
                .setLockQuickConfig()
                .build();
    }
}

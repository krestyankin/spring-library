package ru.krestyankin.library.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    private static final String CHANGELOGS_PACKAGE = "ru.krestyankin.library.changelogs";

    @ConditionalOnProperty(
            value="library.init",
            matchIfMissing = false
    )
    @Bean
    public Mongock mongock(MongoProperties mongoProps, MongoClient mongoClient) {
         return new SpringMongockBuilder(mongoClient, mongoProps.getDatabase(), CHANGELOGS_PACKAGE).build();
    }

}

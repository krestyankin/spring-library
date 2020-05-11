package ru.krestyankin.library.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.krestyankin.library.repositories.BookRepository;

@Component
public class DataHealthIndicator implements HealthIndicator {
    private final BookRepository bookRepository;

    public DataHealthIndicator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Health health() {
        boolean dataExists = bookRepository.count()>0;
        if (!dataExists)
            return Health.down().withDetail("message", "Database is empty").build();
        return Health.up().withDetail("message", "Data is OK").build();
    }
}
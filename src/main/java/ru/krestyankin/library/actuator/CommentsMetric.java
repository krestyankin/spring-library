package ru.krestyankin.library.actuator;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import ru.krestyankin.library.repositories.CommentRepository;

@Component
public class CommentsMetric {
    public CommentsMetric(MeterRegistry registry, CommentRepository commentRepository) {
        Gauge.builder("comments.count", () -> commentRepository.count()).register(registry);
    }
}
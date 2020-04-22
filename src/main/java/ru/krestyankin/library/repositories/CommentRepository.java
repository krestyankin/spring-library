package ru.krestyankin.library.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.krestyankin.library.models.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findAllByBook(String bookId);
    Mono<Void> deleteAllByBook(String bookId);
}

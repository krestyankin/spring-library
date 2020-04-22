package ru.krestyankin.library.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.models.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Flux<Book> findByTitleIsLike(String title);
    Flux<Book> findByAuthors(Mono<Author> author);
 }

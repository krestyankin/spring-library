package ru.krestyankin.library.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.krestyankin.library.models.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
    Flux<Author> findByFullname(String fullname);

}

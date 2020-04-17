package ru.krestyankin.library.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.krestyankin.library.models.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
}

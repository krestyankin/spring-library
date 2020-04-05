package ru.krestyankin.library.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.krestyankin.library.models.Genre;

public interface GenreRepository extends MongoRepository<Genre, String> {
}

package ru.krestyankin.library.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.krestyankin.library.models.Author;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, String> {
    List<Author> findByFullname(String fullname);

}

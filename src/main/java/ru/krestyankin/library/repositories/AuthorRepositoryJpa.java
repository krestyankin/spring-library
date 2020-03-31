package ru.krestyankin.library.repositories;

import ru.krestyankin.library.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepositoryJpa {
    Author save(Author author);
    Optional<Author> findById(long id);

    List<Author> findAll();
    List<Author> findByFullname(String fullname);

    void deleteById(long id);
    long count();
}

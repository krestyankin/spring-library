package ru.krestyankin.library.repositories;

import ru.krestyankin.library.models.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepositoryJpa {
    Genre save(Genre genre);
    Optional<Genre> findById(long id);
    List<Genre> findAll();
    void deleteById(long id);
    long count();
}

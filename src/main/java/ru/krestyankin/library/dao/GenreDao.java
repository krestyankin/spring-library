package ru.krestyankin.library.dao;

import ru.krestyankin.library.domain.Genre;

public interface GenreDao {
    void create(Genre author);
    Genre getById(long id);
    void update(Genre author);
    void deleteById(long id);
    int count();
}

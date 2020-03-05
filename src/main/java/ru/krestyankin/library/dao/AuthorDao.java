package ru.krestyankin.library.dao;

import ru.krestyankin.library.domain.Author;

public interface AuthorDao {
    void create(Author author);
    Author getById(long id);
    void update(Author author);
    void deleteById(long id);
    int count();
}

package ru.krestyankin.library.dao;

import ru.krestyankin.library.domain.Book;

public interface BookDao {
    void create(Book book);
    Book getById(long id);
    void update(Book book);
    void deleteById(long id);
    int count();
}

package ru.krestyankin.library.service;

import ru.krestyankin.library.domain.Book;

public interface BookService {
    Book getById(long bookId);
    void add();
    void update(long bookId);
    void delete(long bookId);
}

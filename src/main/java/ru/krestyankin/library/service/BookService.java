package ru.krestyankin.library.service;

public interface BookService {
    void getById(String bookId);
    void add();
    void update(String bookId);
    void delete(String bookId);
    void all();
    void findByTitle(String title);
    void findByAuthor();
}

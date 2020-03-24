package ru.krestyankin.library.service;

public interface BookService {
    void getById(long bookId);
    void add();
    void update(long bookId);
    void delete(long bookId);
    void addComment(long bookId);
    void deleteComment(long bookId);
    void all();
    void findByTitle(String title);
}

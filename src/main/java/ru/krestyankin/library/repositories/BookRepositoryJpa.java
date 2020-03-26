package ru.krestyankin.library.repositories;

import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryJpa {
    Book save(Book book);
    Optional<Book> findById(long id);

    List<Book> findAll();
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(Author author);

    void deleteById(long id);
    long count();
}

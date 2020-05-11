package ru.krestyankin.library.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.access.prepost.PostFilter;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findAll();

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findByTitleIsLike(String title);

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findByAuthors(Author author);

    Optional<Book> findById(String bookId);
}

package ru.krestyankin.library.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.models.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByTitleIsLike(String title);
    List<Book> findByAuthors(Author author);
}

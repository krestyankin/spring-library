package ru.krestyankin.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.models.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleIsLike(String title);
    List<Book> findByAuthors(Author author);
}

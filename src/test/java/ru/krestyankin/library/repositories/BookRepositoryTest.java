package ru.krestyankin.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.krestyankin.library.repositories", "ru.krestyankin.library.event"})
@DisplayName("BookRepository должен ")
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CommentRepository commentRepository;

    @DisplayName(" искать книгу по названию")
    @Test
    void findByTitleIsLike() {
        Book book = new Book();
        book.setTitle("Some Java Book");
        bookRepository.save(book);
        assertThat(bookRepository.findByTitleIsLike("*Java*")).hasSize(1);
    }

    @DisplayName(" искать книгу по автору")
    @Test
    void findByAuthors() {
        Author author = new Author();
        author.setFullname("Test author");
        author=authorRepository.save(author);
        Book book = new Book();
        book.setTitle("Some Java Book");
        book.setAuthors(List.of(author));
        book=bookRepository.save(book);
        assertThat(bookRepository.findByAuthors(author)).hasSize(1);
    }

    @DisplayName(" должен удалять книгу вместе с комментариями")
    @Test
    void shouldCorrectDeleteBook() {
        Book book = new Book();
        book.setTitle("Some Java Book");
        book=bookRepository.save(book);
        commentRepository.save(new Comment("Test comment 1", book));
        commentRepository.save(new Comment("Test comment 2", book));
        long initialCommentsCount = commentRepository.count();
        long initialBooksCount = bookRepository.count();
        bookRepository.delete(book);
        assertThat(commentRepository.count()).isEqualTo(initialCommentsCount-2);
        assertThat(bookRepository.count()).isEqualTo(initialBooksCount-1);
    }

}
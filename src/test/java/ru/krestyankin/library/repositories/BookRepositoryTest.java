package ru.krestyankin.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.krestyankin.library.config", "ru.krestyankin.library.repositories", "ru.krestyankin.library.event"})
@DisplayName("BookRepository должен ")
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CommentRepository commentRepository;

    @DisplayName(" искать книгу по названию")
    @Test
    void findByTitleIsLike() {
        assertThat(bookRepository.findByTitleIsLike("*Java*")).hasSize(1);
    }

    @DisplayName(" искать книгу по автору")
    @Test
    void findByAuthors() {
        Book book = bookRepository.findAll().get(0);
        assertThat(bookRepository.findByAuthors(book.getAuthors().iterator().next())).hasSize(1).
                allMatch(b -> b.getId().equals(book.getId()));
    }

    @DisplayName(" должен удалять книгу вместе с комментариями")
    @Test
    void shouldCorrectDeleteBook() {
        Book book = bookRepository.findAll().get(1);
        commentRepository.save(new Comment("Test comment 1", book));
        commentRepository.save(new Comment("Test comment 2", book));
        long initialCommentsCount = commentRepository.count();
        long initialBooksCount = bookRepository.count();
        bookRepository.delete(book);
        assertThat(commentRepository.count()).isEqualTo(initialCommentsCount-2);
        assertThat(bookRepository.count()).isEqualTo(initialBooksCount-1);
    }

}
package ru.krestyankin.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;


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
        StepVerifier.create(bookRepository.findByTitleIsLike("*Java*"))
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @DisplayName(" искать книгу по автору")
    @Test
    void findByAuthors() {
        Mono<Book> book=bookRepository.findAll().next();
        Mono<Author> author = book.map(b -> b.getAuthors().get(0));
        StepVerifier.create(bookRepository.findByAuthors(author))
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @DisplayName(" должен удалять книгу вместе с комментариями")
    @Test
    void shouldCorrectDeleteBook() {
        Mono<Book> book=bookRepository.findAll().elementAt(1);
        Mono<Book> bookWithComments = book.flatMap(b ->
                Mono.from(commentRepository.save(new Comment("Test comment 1", b)))
                        .then(commentRepository.save(new Comment("Test comment 2", b)))
                        .then(Mono.just(b)));
        StepVerifier.create(Flux.from(bookWithComments).thenMany(book.flatMapMany(b->commentRepository.findAllByBook(b.getId()))))
                .expectNextCount(2)
                .expectComplete().verify();

        Mono<String> deletedBookId = book.flatMap(b-> Mono.from(bookRepository.deleteById(b.getId())).
                then(Mono.just(b.getId())));
        StepVerifier.create(deletedBookId.flatMapMany(b->commentRepository.findAllByBook(b)))
                .expectNextCount(0).expectComplete().verify();

    }

}
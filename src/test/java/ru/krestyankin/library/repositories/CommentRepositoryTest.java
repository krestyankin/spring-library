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
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.krestyankin.library.config", "ru.krestyankin.library.repositories", "ru.krestyankin.library.event"})
@DisplayName("CommentRepository должен ")
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    BookRepository bookRepository;

    @DisplayName(" добавлять и удалять комментарии")
    @Test
    void shouldCorrectAddComment(){
        Mono<Book> book=bookRepository.findAll().elementAt(1);
        StepVerifier.create(book.flatMapMany(book1 -> Flux.from(commentRepository.save(new Comment("Comment text", book1)))
                .thenMany(commentRepository.findAllByBook(book1.getId()))))
                .recordWith(ArrayList::new)
                .expectNextCount(1)
                .consumeRecordedWith(comments -> assertThat(comments).isNotNull().hasSize(1).
                        allMatch(comment -> comment.getId()!=null && comment.getText().equals("Comment text")))
                .expectComplete()
                .verify();
        StepVerifier.create(book.flatMapMany(book1 -> {
            Mono<Comment> comment = commentRepository.findAllByBook(book1.getId()).next();
            return Flux.from(comment.flatMap(comment1 -> commentRepository.delete(comment1)))
                                .thenMany(commentRepository.findAllByBook(book1.getId())); }
                )).expectNextCount(0).expectComplete().verify();
    }

    @DisplayName(" удалять все комментарии книги")
    @Test
    void shouldCorrectDeleteAllBookComments() {
        Mono<Book> book=bookRepository.findAll().elementAt(0);
        StepVerifier.create(book.flatMapMany(book1 -> Flux.from(commentRepository.deleteAllByBook(book1.getId()))
                .thenMany(commentRepository.findAllByBook(book1.getId())))).expectNextCount(0)
                .expectComplete().verify();

    }

    @DisplayName(" должен корректно получать комментарии книги")
    @Test
    void shouldCorrectFetchBookComment() {
        Mono<Book> book=bookRepository.findAll().elementAt(2);
        StepVerifier.create(book.flatMapMany(book1 -> commentRepository.findAllByBook(book1.getId())))
                .recordWith(ArrayList::new)
                .expectNextCount(3)
                .consumeRecordedWith(comments -> assertThat(comments).isNotNull().hasSize(3).
                              allMatch(comment -> comment.getId()!=null && comment.getText().startsWith("Комментарий ")))
                .expectComplete()
                .verify();
    }


}
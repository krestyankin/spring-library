package ru.krestyankin.library.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.krestyankin.library.repositories.AuthorRepository;
import ru.krestyankin.library.repositories.BookRepository;
import ru.krestyankin.library.repositories.CommentRepository;
import ru.krestyankin.library.repositories.GenreRepository;

import static org.mockito.Mockito.times;

@WebFluxTest(CommentController.class)
class CommentControllerTest {
    @Autowired
    private WebTestClient webClient;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;

    @Test
    void getAll() {
        webClient.get().uri("/comment/get/book1").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk();
        Mockito.verify(commentRepository, times(1)).findAllByBook("book1");
    }

    @Test
    void delete() {
        webClient.delete().uri("/comment/delete/comment1").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk();
        Mockito.verify(commentRepository, times(1)).deleteById("comment1");
    }

}
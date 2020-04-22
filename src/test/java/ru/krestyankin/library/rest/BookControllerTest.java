package ru.krestyankin.library.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.repositories.AuthorRepository;
import ru.krestyankin.library.repositories.BookRepository;
import ru.krestyankin.library.repositories.CommentRepository;
import ru.krestyankin.library.repositories.GenreRepository;
import ru.krestyankin.library.service.BookDtoConverter;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@WebFluxTest({BookController.class, BookDtoConverter.class})
class BookControllerTest {
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


    private static final List<String> BOOK_TITLES = List.of("Book12345","AnotherBook54321");

    @Test
    void viewPage() {
        given(bookRepository.findById("book1"))
                .willReturn(Mono.just(new Book("book1", BOOK_TITLES.get(0), null, null)));
        webClient.get().uri(uriBuilder -> uriBuilder.path("/book/view").queryParam("id", "book1").build())
                .accept(MediaType.TEXT_HTML).exchange()
                .expectStatus().isOk().expectBody(String.class).value(containsString(BOOK_TITLES.get(0)));
    }

    @Test
    void deletePage() {
        webClient.get().uri(uriBuilder -> uriBuilder.path("/book/delete").queryParam("id", "book1").build())
                .accept(MediaType.TEXT_HTML).exchange()
                .expectStatus().isOk();
        Mockito.verify(bookRepository, times(1)).deleteById("book1");
    }
}
package ru.krestyankin.library.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;
import ru.krestyankin.library.models.Genre;
import ru.krestyankin.library.repositories.*;
import ru.krestyankin.library.service.BookDtoConverter;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookController.class, BookDtoConverter.class})
class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private UserRepository userRepository;


    private static final List<String> BOOK_TITLES = List.of("Book12345","AnotherBook54321");
    private static final String COMMENT_TEXT = "CommentText12345";

    @Test
    void listPage() throws Exception {
        given(bookRepository.findAll()).willReturn(List.of(new Book("book1", BOOK_TITLES.get(0), null, null),
                new Book("book2", BOOK_TITLES.get(1), null, null))
        );
        mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(containsString(BOOK_TITLES.get(0))))
                .andExpect(content().string(containsString(BOOK_TITLES.get(1))));
    }

    @Test
    void viewPage() throws Exception {
        given(bookRepository.findById("book1"))
                .willReturn(java.util.Optional.of(new Book("book1", BOOK_TITLES.get(0), null, null)));
        given(commentRepository.getCommentsByBook("book1")).willReturn(List.of(new Comment(COMMENT_TEXT, null)));
        mvc.perform(get("/book/view").param("id", "book1")).andExpect(status().isOk())
                .andExpect(content().string(containsString(BOOK_TITLES.get(0))))
                .andExpect(content().string(containsString(COMMENT_TEXT)));
        mvc.perform(get("/book/view").param("id", "book2")).andExpect(status().isNotFound());
    }

    @WithMockUser(
            username = "admin"
    )
    @Test
    void editPage() throws Exception {
        given(bookRepository.findById("book1"))
                .willReturn(java.util.Optional.of(new Book("book1", BOOK_TITLES.get(0), List.of(new Author()), List.of(new Genre()))));
        mvc.perform(get("/book/edit").param("id", "book1")).andExpect(status().isOk())
                .andExpect(content().string(containsString(BOOK_TITLES.get(0))));
    }

    @WithMockUser(
            username = "admin"
    )
    @Test
    void savePage() throws Exception {
        mvc.perform(post("/book/save")
                .param("id", "book")
                .param("title", BOOK_TITLES.get(0))
                .param("authors", "")
                .param("genres", "")
                .with(csrf())
        ).andExpect(status().isOk());
        Mockito.verify(bookRepository, times(1)).save(Mockito.any());
    }

    @WithMockUser(
            username = "admin"
    )
    @Test
    void deletePage() throws Exception {
        mvc.perform(get("/book/delete").param("id", "book1")).andExpect(status().isOk());
        Mockito.verify(bookRepository, times(1)).deleteById("book1");
    }

    @WithMockUser(
            username = "admin"
    )
    @Test
    void addPage() throws Exception {
        mvc.perform(get("/book/add")).andExpect(status().isOk());
    }
}
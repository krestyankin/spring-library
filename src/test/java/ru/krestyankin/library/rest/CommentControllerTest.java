package ru.krestyankin.library.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;
import ru.krestyankin.library.repositories.AuthorRepository;
import ru.krestyankin.library.repositories.BookRepository;
import ru.krestyankin.library.repositories.CommentRepository;
import ru.krestyankin.library.repositories.GenreRepository;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {
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

    @Test
    void getAll() throws Exception {
        mvc.perform(get("/comment/get/{bookId}", "book1")).andExpect(status().isOk());
        Mockito.verify(commentRepository, times(1)).findAllByBook("book1");
    }

    @Test
    void add() throws Exception {
        Book book = new  Book("book1", "Book title", null, null);
        given(bookRepository.findById(book.getId())).willReturn(java.util.Optional.of(book));
        Comment comment = new Comment("Text", book);
        mvc.perform(post("/comment/add").param("bookId", comment.getBook().getId()).param("text", comment.getText())).andExpect(status().isOk());
        Mockito.verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/comment/delete/{commentId}", "comment1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(commentRepository, times(1)).deleteById("comment1");
    }
}
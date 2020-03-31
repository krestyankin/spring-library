package ru.krestyankin.library.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Genre;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookRepositoryImplTest {
    private static final int EXPECTED_NUMBER_OF_BOOKS=3;
    private static final long BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS = 3;
    private static final String BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS_TITLE="Book with 3 genres by 2 authors";
    private static final String BOOK_TITLE="TEST";
    private static final long BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS_COMMENTS = 2;
    private static final long NEW_BOOK_ID=4;
    private static final String BOOK_TITLE_LIKE="Simple%";
    private static final String BOOK_TITLE_EXPECTED="Simple book";

    @Autowired
    private BookRepository repositoryJpa;

    @Autowired
    private AuthorRepository authorRepositoryJpa;

    @Test
    void save() {
        long count = repositoryJpa.count();
        Book book = new Book();
        book.setTitle(BOOK_TITLE);
        book.setAuthors(Collections.singleton(authorRepositoryJpa.findById(1L).get()));
        book.setGenres(Collections.singleton(new Genre(1, "genre 1")));
        repositoryJpa.save(book);
        assertEquals(count+1, repositoryJpa.count());
        book=repositoryJpa.findById(NEW_BOOK_ID).get();
        assertEquals(BOOK_TITLE, book.getTitle());
        assertEquals(1, book.getAuthors().size());
        assertEquals(1, book.getGenres().size());
    }

    @Test
    void findById() {
        Book book = repositoryJpa.findById(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS).get();
        assertEquals(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS_TITLE, book.getTitle());
        assertEquals(2, book.getAuthors().size());
        assertEquals(3, book.getGenres().size());
        assertEquals(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS_COMMENTS, book.getComments().size());
    }

    @Test
    void findAll() {
        List<Book> books = repositoryJpa.findAll();
        assertThat(books).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
                .allMatch(book -> !book.getTitle().equals(""))
                .allMatch(book -> book.getAuthors()!=null && book.getAuthors().size()>0)
                .allMatch(book -> book.getGenres()!=null && book.getGenres().size()>0);
    }

    @Test
        void findByTitle() {
        List<Book> books = repositoryJpa.findByTitleIsLike(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS_TITLE);
        assertThat(books).isNotNull().hasSize(1);
    }

    @Test
    void deleteById() {
        long count = repositoryJpa.count();
        repositoryJpa.deleteById(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS);
        assertEquals(count-1, repositoryJpa.count());
        List<Book> books = repositoryJpa.findByTitleIsLike(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS_TITLE);
        assertThat(books).isNotNull().hasSize(0);
    }

    @Test
    void count() {
        assertThat(repositoryJpa.count()).isEqualTo(EXPECTED_NUMBER_OF_BOOKS);
    }

    @Test
    void findByAuthors() {
        List<Book> books = repositoryJpa.findByAuthors(authorRepositoryJpa.findById(3L).get());
        assertThat(books).isNotNull().hasSize(1).allMatch(book-> book.getAuthors()!=null && book.getAuthors().size()==2);
    }

    @Test
    void findByFullname() {
        List<Book> books = repositoryJpa.findByTitleIsLike(BOOK_TITLE_LIKE);
        assertThat(books).isNotNull().hasSize(1).allMatch(book -> book.getTitle().equals(BOOK_TITLE_EXPECTED));
    }
}
package ru.krestyankin.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.krestyankin.library.domain.Author;
import ru.krestyankin.library.domain.Book;
import ru.krestyankin.library.domain.Genre;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Jdbc-репозиторий для книг")
@SpringBootTest
@Transactional
class BookDaoJdbcTest {
    private static final long NUMBER_OF_BOOKS=3;
    private static final long BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS = 3;
    private static final String BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS_TITLE="Book with 3 genres by 2 authors";
    private static final String BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS_NEW_TITLE="TEST";
    private static final long NEW_BOOK_ID=10;
    private static final String NEW_BOOK_TITLE="NEW BOOK";
    @Autowired
    private BookDao bookDao;

    @Test
    void insert() {
        int count = bookDao.count();
        Book book = new Book(NEW_BOOK_ID, NEW_BOOK_TITLE,
                Arrays.asList(new Author(1,"Author 1", new Date())), Arrays.asList(new Genre(1, "genre 1")));
        bookDao.create(book);
        book=bookDao.getById(NEW_BOOK_ID);
        assertEquals(count+1, bookDao.count());
        assertEquals(NEW_BOOK_TITLE, book.getTitle());

    }

    @Test
    void getById() {
        Book book = bookDao.getById(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS);
        assertEquals(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS_TITLE, book.getTitle());
        assertEquals(2, book.getAuthors().size());
        assertEquals(3, book.getGenres().size());
    }

    @Test
    void update() {
        Book book = bookDao.getById(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS);
        book.setTitle(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS_NEW_TITLE);
        book.getAuthors().remove(0);
        book.getGenres().remove(0);
        bookDao.update(book);
        book = bookDao.getById(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS);
        assertEquals(BOOK_WITH_THREE_GENRES_BY_TWO_AUTHORS_NEW_TITLE, book.getTitle());
        assertEquals(1, book.getAuthors().size());
        assertEquals(2, book.getGenres().size());
    }

    @Test
    void deleteById() {
        int count = bookDao.count();
        bookDao.deleteById(1);
        assertEquals(count-1, bookDao.count());
    }

    @Test
    void count() {
        assertEquals(NUMBER_OF_BOOKS, bookDao.count());
    }
}
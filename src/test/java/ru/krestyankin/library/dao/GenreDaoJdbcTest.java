package ru.krestyankin.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.krestyankin.library.domain.Genre;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Jdbc-репозиторий для жанров")
@SpringBootTest
@Transactional
class GenreDaoJdbcTest {
    private static final long GENRE_ID = 1;
    private static final String GENRE_NAME = "genre 1";
    private static final long GENRE_ID_NEW = 5;
    private static final String GENRE_NAME_NEW = "Test";

    @Autowired
    private GenreDao genreDao;

    @Test
    void insert() {
        int count = genreDao.count();
        Genre genre = new Genre(GENRE_ID_NEW, GENRE_NAME_NEW);
        genreDao.create(genre);
        assertEquals(count+1, genreDao.count());
        genre = genreDao.getById(GENRE_ID_NEW);
        assertEquals(GENRE_ID_NEW, genre.getId());
        assertEquals(GENRE_NAME_NEW, genre.getName());
    }

    @Test
    void getById() {
        Genre genre = genreDao.getById(GENRE_ID);
        assertEquals(GENRE_ID, genre.getId());
        assertEquals(GENRE_NAME, genre.getName());
    }

    @Test
    void update() {
        Genre genre = genreDao.getById(GENRE_ID);
        genre.setName(GENRE_NAME_NEW);
        genreDao.update(genre);
        genre = genreDao.getById(GENRE_ID);
        assertEquals(GENRE_NAME_NEW, genre.getName());
    }

    @Test
    void deleteById() {
        int count = genreDao.count();
        genreDao.deleteById(1);
        assertEquals(count-1, genreDao.count());
    }

    @Test
    void count() {
        assertEquals(3, genreDao.count());
    }
}
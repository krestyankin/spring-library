package ru.krestyankin.library.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.krestyankin.library.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(GenreRepositoryJpaImpl.class)
class GenreRepositoryJpaImplTest {
    private static final long GENRE_ID = 1;
    private static final String GENRE_NAME = "genre 1";
    private static final long GENRE_ID_NEW = 4;
    private static final String GENRE_NAME_NEW = "New name";
    private static final int EXPECTED_NUMBER_OF_GENRES=3;

    @Autowired
    private GenreRepositoryJpa repositoryJpa;


    @Test
    void save() {
        long count = repositoryJpa.count();
        Genre genre = new Genre();
        genre.setName(GENRE_NAME);
        repositoryJpa.save(genre);
        assertEquals(count+1, repositoryJpa.count());
        genre = repositoryJpa.findById(GENRE_ID_NEW).get();
        assertEquals(GENRE_ID_NEW, genre.getId());
        assertEquals(GENRE_NAME, genre.getName());
    }

    @Test
    void findById() {
        Genre genre = repositoryJpa.findById(GENRE_ID).get();
        assertEquals(GENRE_ID, genre.getId());
        assertEquals(GENRE_NAME, genre.getName());
    }

    @Test
    void findAll() {
        List<Genre> genres = repositoryJpa.findAll();
        assertThat(genres).isNotNull().hasSize(EXPECTED_NUMBER_OF_GENRES)
                .allMatch(genre -> !genre.getName().equals(""));
    }

    @Test
    void deleteById() {
        long count = repositoryJpa.count();
        repositoryJpa.deleteById(GENRE_ID);
        assertEquals(count-1, repositoryJpa.count());
        assertThat(repositoryJpa.findById(GENRE_ID)).isEqualTo(Optional.empty());
    }
}
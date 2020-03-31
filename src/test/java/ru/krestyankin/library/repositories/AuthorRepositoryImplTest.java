package ru.krestyankin.library.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.krestyankin.library.models.Author;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class AuthorRepositoryImplTest {
    private static final long AUTHOR_ID = 1;
    private static final String AUTHOR_NAME = "Author 1";
    private static final long AUTHOR_ID_NEW = 5;
    private static final String AUTHOR_NAME_NEW = "Test";
    private static final int EXPECTED_NUMBER_OF_AUTHORS = 4;

    @Autowired
    private AuthorRepository repositoryJpa;

    @Test
    void save() {
        long count = repositoryJpa.count();
        Author author = new Author();
        author.setFullname(AUTHOR_NAME_NEW);
        author.setDateOfBirth(new Date());
        repositoryJpa.save(author);
        assertEquals(count+1, repositoryJpa.count());
        author = repositoryJpa.findById(AUTHOR_ID_NEW).get();
        assertEquals(AUTHOR_ID_NEW, author.getId());
        assertEquals(AUTHOR_NAME_NEW, author.getFullname());
    }

    @Test
    void findById() {
        Author author = repositoryJpa.findById(AUTHOR_ID).get();
        assertEquals(AUTHOR_ID, author.getId());
        assertEquals(AUTHOR_NAME, author.getFullname());
    }

    @Test
    void findAll() {
        List<Author> authors = repositoryJpa.findAll();
        assertThat(authors).isNotNull().hasSize(EXPECTED_NUMBER_OF_AUTHORS)
                .allMatch(author -> !author.getFullname().equals(""));
    }

    @Test
    void findByFullname() {
        List<Author> authors = repositoryJpa.findByFullname(AUTHOR_NAME);
        assertThat(authors).isNotNull().hasSize(1);
    }

    @Test
    void deleteById() {
        long count = repositoryJpa.count();
        repositoryJpa.deleteById(AUTHOR_ID);
        assertEquals(count-1, repositoryJpa.count());
        List<Author> authors = repositoryJpa.findByFullname(AUTHOR_NAME);
        assertThat(authors).isNotNull().hasSize(0);
    }

    @Test
    void count() {
        assertThat(repositoryJpa.count()).isEqualTo(EXPECTED_NUMBER_OF_AUTHORS);
    }
}
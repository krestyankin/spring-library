package ru.krestyankin.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.krestyankin.library.domain.Author;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Jdbc-репозиторий для авторов")
@SpringBootTest
@Transactional
class AuthorDaoJdbcTest {
    private static final long AUTHOR_ID = 1;
    private static final String AUTHOR_NAME = "Author 1";
    private static final long AUTHOR_ID_NEW = 5;
    private static final String AUTHOR_NAME_NEW = "Test";

    @Autowired
    private AuthorDao authorDao;

    @Test
    void insert() {
        int count = authorDao.count();
        Author author = new Author(AUTHOR_ID_NEW, AUTHOR_NAME_NEW, new Date());
        authorDao.create(author);
        assertEquals(count+1, authorDao.count());
        author = authorDao.getById(AUTHOR_ID_NEW);
        assertEquals(AUTHOR_ID_NEW, author.getId());
        assertEquals(AUTHOR_NAME_NEW, author.getFullname());
    }

    @Test
    void getById() {
        Author author = authorDao.getById(AUTHOR_ID);
        assertEquals(AUTHOR_ID, author.getId());
        assertEquals(AUTHOR_NAME, author.getFullname());
    }

    @Test
    void update() {
        Author author = authorDao.getById(AUTHOR_ID);
        author.setFullname(AUTHOR_NAME_NEW);
        authorDao.update(author);
        author = authorDao.getById(AUTHOR_ID);
        assertEquals(AUTHOR_NAME_NEW, author.getFullname());
    }

    @Test
    void deleteById() {
        int count = authorDao.count();
        authorDao.deleteById(1);
        assertEquals(count-1, authorDao.count());
    }

    @Test
    void count() {
        assertEquals(4, authorDao.count());
    }
}
package ru.krestyankin.library.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.krestyankin.library.domain.Author;
import ru.krestyankin.library.domain.Book;
import ru.krestyankin.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbc;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    private void saveBookRelations(Book book) {
        Map<String, Object> params = new HashMap<>();
        for (Author author: book.getAuthors()) {
            params.clear();
            params.put("book_id", book.getId());
            params.put("author_id", author.getId());
            jdbc.update("insert into books_authors(book_id, author_id) values (:book_id, :author_id)", params);
        }
        for (Genre genre: book.getGenres()) {
            params.clear();
            params.put("book_id", book.getId());
            params.put("genre_id", genre.getId());
            jdbc.update("insert into books_genres(book_id, genre_id) values (:book_id, :genre_id)", params);
        }
    }

    @Override
    public void create(Book book) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", book.getId());
        params.put("title", book.getTitle());
        jdbc.update("insert into books(id, title) values (:id, :title)", params);
        saveBookRelations(book);
    }

    @Override
    public Book getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbc.query(
                "select b.id, b.title, ba.author_id, a.fullname, a.dob, bg.genre_id, g.name genre_name" +
                   "  from books b " +
                   "       join books_authors ba on ba.book_id=b.id" +
                   "       join authors a on a.id=ba.author_id" +
                   "       join books_genres bg on bg.book_id=b.id" +
                   "       join genres g on g.id=bg.genre_id"+
                   " where b.id = :id", params, new BookResultSetExtractor());

    }

    @Override
    public void update(Book book) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", book.getId());
        params.put("title", book.getTitle());
        jdbc.update("update books set title=:title where id=:id", params);
        params = Collections.singletonMap("id", book.getId());
        jdbc.update("delete from books_authors where book_id = :id", params);
        jdbc.update("delete from books_genres where book_id = :id", params);
        saveBookRelations(book);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update("delete from books_authors where book_id = :id", params);
        jdbc.update("delete from books_genres where book_id = :id", params);
        jdbc.update("delete from books where id = :id", params);
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(1) from books", (Map<String, ?>) null, Integer.class);
    }

    private static class BookResultSetExtractor implements ResultSetExtractor<Book>{
        @Override
        public Book extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            resultSet.next();
            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            Map<Long, Author> authors = new HashMap<>();
            Map<Long, Genre> genres = new HashMap<>();
            do {
                Author author = new Author(resultSet.getLong("author_id"), resultSet.getString("fullname"), resultSet.getDate("dob"));
                authors.put(author.getId(), author);
                Genre genre = new Genre(resultSet.getLong("genre_id"), resultSet.getString("genre_name"));
                genres.put(genre.getId(), genre);
            } while (resultSet.next());
            return new Book(id, title, new ArrayList<>(authors.values()), new ArrayList<>(genres.values()));
        }
    }
}

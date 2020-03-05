package ru.krestyankin.library.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.krestyankin.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void create(Genre Genre) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", Genre.getId());
        params.put("name", Genre.getName());
        jdbc.update("insert into genres(id, name) values (:id, :name)", params);
    }

    @Override
    public Genre getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbc.queryForObject("select * from genres where id=:id", params, new GenreMapper());
    }

    @Override
    public void update(Genre Genre) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", Genre.getId());
        params.put("name", Genre.getName());
        jdbc.update("update genres set name=:name where id=:id", params);
    }

    @Override
    public void deleteById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbc.update("delete from genres where id = :id", params);
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(1) from genres", (Map<String, ?>) null, Integer.class);
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Genre(resultSet.getLong("id"), resultSet.getString("name"));
        }
    }

}

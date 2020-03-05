package ru.krestyankin.library.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Book {
    private long id;
    private String title;
    private List<Author> authors;
    private List<Genre> genres;

    public Book(long id, String title, List<Author> authors, List<Genre> genres) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.genres = genres;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", genres=" + genres +
                '}';
    }
}

package ru.krestyankin.library.service;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.krestyankin.library.models.Genre;
import ru.krestyankin.library.repositories.GenreRepository;

import java.util.List;
import java.util.Scanner;

@ShellComponent
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final Scanner in;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
        this.in = new Scanner(System.in);
    }

    @Override
    @ShellMethod(value = "Add genre", key = {"add genre", "ga"})
    public void add() {
        Genre genre = new Genre();
        System.out.print("Название: ");
        genre.setName(in.nextLine());
        genre = genreRepository.save(genre);
        System.out.println(genre);
    }

    @Override
    @ShellMethod(value = "Get all genres", key = {"get genres", "gga"})
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }
}

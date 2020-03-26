package ru.krestyankin.library.service;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.repositories.AuthorRepositoryJpa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

@ShellComponent
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepositoryJpa authorRepositoryJpa;
    private final Scanner in;

    public AuthorServiceImpl(AuthorRepositoryJpa authorRepositoryJpa) {
        this.authorRepositoryJpa = authorRepositoryJpa;
        this.in = new Scanner(System.in);
    }

    @Override
    @ShellMethod(value = "Get author by id", key = {"get author", "ag"})
    public Optional<Author> getById(long authorId) {
        return authorRepositoryJpa.findById(authorId);
    }

    @Override
    @ShellMethod(value = "Add author", key = {"add author", "aa"})
    @Transactional
    public void add() {
        Author author = new Author();
        System.out.print("Имя: ");
        author.setFullname(in.nextLine());
        System.out.print("Дата рождения (дд.мм.гггг): ");
        try {
            author.setDateOfBirth(new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(in.nextLine()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        author = authorRepositoryJpa.save(author);
        System.out.println(author);
    }

    @Override
    @ShellMethod(value = "Edit author", key = {"edit author", "ae"})
    @Transactional
    public void update(long authorId) {
        Author author = authorRepositoryJpa.findById(authorId).orElseThrow(IllegalArgumentException::new);
        System.out.println("Редактирование автора");
        System.out.print("ФИО: ");
        author.setFullname(in.nextLine());
        System.out.print("Дата рождения (дд.мм.гггг): ");
        try {
            author.setDateOfBirth(new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(in.nextLine()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        authorRepositoryJpa.save(author);
    }

    @Override
    @Transactional
    @ShellMethod(value = "Delete author", key = {"delete author", "ad"})
    public void delete(long authorId) {
        authorRepositoryJpa.deleteById(authorId);
    }

    @Override
    @ShellMethod(value = "Get all authors", key = {"get authors", "aga"})
    public List<Author> getAll() {
        return authorRepositoryJpa.findAll();
    }

    @Override
    @ShellMethod(value = "Get authors count", key = {"get authors count", "agc"})
    public void count() {
        System.out.println("Всего авторов " + authorRepositoryJpa.count());
    }
}

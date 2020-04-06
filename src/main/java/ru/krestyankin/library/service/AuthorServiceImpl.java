package ru.krestyankin.library.service;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.repositories.AuthorRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ShellComponent
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final Scanner in;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
        this.in = new Scanner(System.in);
    }

    @Override
    @ShellMethod(value = "Get author by id", key = {"get author", "ag"})
    public Optional<Author> getById(String authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    @ShellMethod(value = "Add author", key = {"add author", "aa"})
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
        author = authorRepository.save(author);
        System.out.println(author);
    }

    @Override
    @ShellMethod(value = "Edit author", key = {"edit author", "ae"})
    @Transactional
    public void update(String authorId) {
        Author author = authorRepository.findById(authorId).orElseThrow(IllegalArgumentException::new);
        System.out.println("Редактирование автора");
        System.out.print("ФИО: ");
        author.setFullname(in.nextLine());
        System.out.print("Дата рождения (дд.мм.гггг): ");
        try {
            author.setDateOfBirth(new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(in.nextLine()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        authorRepository.save(author);
    }

    @Override
    @Transactional
    @ShellMethod(value = "Delete author", key = {"delete author", "ad"})
    public void delete(String authorId) {
        authorRepository.deleteById(authorId);
    }

    @Override
    @ShellMethod(value = "Get all authors", key = {"get authors", "aga"})
    public List<Author> getAll() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    @ShellMethod(value = "Get authors count", key = {"get authors count", "agc"})
    public void count() {
        System.out.println("Всего авторов " + authorRepository.count());
    }
}

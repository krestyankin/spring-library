package ru.krestyankin.library.service;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.krestyankin.library.dao.AuthorDao;
import ru.krestyankin.library.domain.Author;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

@ShellComponent
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDao;
    private final Scanner in;

    public AuthorServiceImpl(AuthorDao authorDao) {
        this.authorDao = authorDao;
        this.in = new Scanner(System.in);
    }

    @Override
    @ShellMethod(value = "Get author by id", key = {"get author", "ga"})
    public Author getById(long authorId) {
        return authorDao.getById(authorId);
    }

    @Override
    @ShellMethod(value = "Add author", key = {"add author", "aa"})
    public void add() {
        long authorId;
        String authorName;
        Date authorDob = null;
        System.out.println("Добавление автора");
        System.out.print("ИД: ");
        authorId = Long.parseLong(in.nextLine());
        System.out.print("Имя: ");
        authorName = in.nextLine();
        System.out.print("Дата рождения (дд.мм.гггг): ");
        try {
            authorDob =  new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(in.nextLine());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Author author = new Author(authorId, authorName, authorDob);
        authorDao.create(author);
        System.out.println(author);
    }

    @Override
    @ShellMethod(value = "Edit author", key = {"edit author", "ea"})
    public void update(long authorId) {
        Author author = authorDao.getById(authorId);
        System.out.println("Редактирование автора");
        System.out.print("ФИО: ");
        author.setFullname(in.nextLine());
        System.out.print("Дата рождения (дд.мм.гггг): ");
        try {
            author.setDateOfBirth(new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(in.nextLine()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        authorDao.update(author);
    }

    @Override
    public void delete(long authorId) {
        authorDao.deleteById(authorId);
    }
}

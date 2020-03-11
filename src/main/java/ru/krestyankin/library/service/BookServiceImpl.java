package ru.krestyankin.library.service;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.krestyankin.library.dao.AuthorDao;
import ru.krestyankin.library.dao.BookDao;
import ru.krestyankin.library.dao.GenreDao;
import ru.krestyankin.library.domain.Book;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

@ShellComponent
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final Scanner in;

    public BookServiceImpl(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.in = new Scanner(System.in);
    }

    @Override
    @ShellMethod(value = "Get book", key = {"get book", "gb"})
    public Book getById(long bookId) {
        return bookDao.getById(bookId);
    }

    @Override
    @ShellMethod(value = "Add book", key = {"add book", "ab"})
    public void add() {
        System.out.println("Добавление книги");
        System.out.print("ИД: ");
        long bookId = Long.parseLong(in.nextLine());
        System.out.print("Название: ");
        String title = in.nextLine();
        System.out.print("Автор(ы), ИД через запятую: ");
        String authors = in.nextLine();
        System.out.print("Жанр(ы), ИД через запятую: ");
        String genres = in.nextLine();
        bookDao.create(new Book(bookId, title,
                Arrays.stream(authors.split("[, ]+")).map(authorId -> authorDao.getById(Long.parseLong(authorId))).collect(Collectors.toList()),
                Arrays.stream(genres.split("[, ]+")).map(genreId -> genreDao.getById(Long.parseLong(genreId))).collect(Collectors.toList())));
    }

    @Override
    @ShellMethod(value = "Edit book", key = {"edit book", "eb"})
    public void update(long bookId) {
        System.out.println("Редактирование книги");
        Book book = bookDao.getById(bookId);
        System.out.println(book);
        System.out.print("Название: ");
        book.setTitle(in.nextLine());
        System.out.print("Автор(ы), ИД через запятую: ");
        book.setAuthors(Arrays.stream(in.nextLine().split("[, ]+")).map(authorId -> authorDao.getById(Long.parseLong(authorId))).collect(Collectors.toList()));
        System.out.print("Жанр(ы), ИД через запятую: ");
        book.setGenres(Arrays.stream(in.nextLine().split("[, ]+")).map(genreId -> genreDao.getById(Long.parseLong(genreId))).collect(Collectors.toList()));
        bookDao.update(book);
    }

    @Override
    @ShellMethod(value = "Delete book", key = {"delete book", "db"})
    public void delete(long bookId) {
        bookDao.deleteById(bookId);
    }
}

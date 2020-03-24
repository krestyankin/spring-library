package ru.krestyankin.library.service;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.transaction.annotation.Transactional;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;
import ru.krestyankin.library.repositories.AuthorRepositoryJpa;
import ru.krestyankin.library.repositories.BookRepositoryJpa;
import ru.krestyankin.library.repositories.GenreRepositoryJpa;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

@ShellComponent
public class BookServiceImpl implements BookService {
    private final BookRepositoryJpa bookRepositoryJpa;
    private final AuthorRepositoryJpa authorRepositoryJpa;
    private final GenreRepositoryJpa genreRepositoryJpa;
    private final Scanner in;

    public BookServiceImpl(BookRepositoryJpa bookRepositoryJpa, AuthorRepositoryJpa authorRepositoryJpa, GenreRepositoryJpa genreRepositoryJpa) {
        this.bookRepositoryJpa = bookRepositoryJpa;
        this.authorRepositoryJpa = authorRepositoryJpa;
        this.genreRepositoryJpa = genreRepositoryJpa;
        this.in = new Scanner(System.in);
    }

    @Override
    @ShellMethod(value = "Get book", key = {"get book", "bg"})
    @Transactional
    public void getById(long bookId) {
        Book book = bookRepositoryJpa.findById(bookId).get();
        System.out.println("Книга: ");
        System.out.println(book);
        System.out.println("Комментарии: ");
        System.out.println(book.getComments());
    }

    @Override
    @ShellMethod(value = "Add book", key = {"add book", "ba"})
    public void add() {
        System.out.println("Добавление книги");
        Book book = new Book();
        System.out.print("Название: ");
        book.setTitle(in.nextLine());
        System.out.print("Автор(ы), ИД через запятую: ");
        book.setAuthors(Arrays.stream(in.nextLine().split("[, ]+")).map(authorId -> authorRepositoryJpa.findById(Long.parseLong(authorId)).orElseThrow(IllegalArgumentException::new)).collect(Collectors.toSet()));
        System.out.print("Жанр(ы), ИД через запятую: ");
        book.setGenres(Arrays.stream(in.nextLine().split("[, ]+")).map(genreId -> genreRepositoryJpa.findById(Long.parseLong(genreId)).orElseThrow(IllegalArgumentException::new)).collect(Collectors.toSet()));
        bookRepositoryJpa.save(book);
    }

    @Override
    @ShellMethod(value = "Edit book", key = {"edit book", "be"})
    public void update(long bookId) {
        System.out.println("Редактирование книги");
        Book book = bookRepositoryJpa.findById(bookId).orElseThrow(IllegalArgumentException::new);
        System.out.print("Название: ");
        book.setTitle(in.nextLine());
        System.out.print("Автор(ы), ИД через запятую: ");
        book.setAuthors(Arrays.stream(in.nextLine().split("[, ]+")).map(authorId -> authorRepositoryJpa.findById(Long.parseLong(authorId)).orElseThrow(IllegalArgumentException::new)).collect(Collectors.toSet()));
        System.out.print("Жанр(ы), ИД через запятую: ");
        book.setGenres(Arrays.stream(in.nextLine().split("[, ]+")).map(genreId -> genreRepositoryJpa.findById(Long.parseLong(genreId)).orElseThrow(IllegalArgumentException::new)).collect(Collectors.toSet()));
        bookRepositoryJpa.save(book);
    }

    @Override
    @ShellMethod(value = "Delete book", key = {"delete book", "bd"})
    public void delete(long bookId) {
        bookRepositoryJpa.deleteById(bookId);
    }

    @Override
    @ShellMethod(value = "Add comment", key = {"add comment", "bca"})
    @Transactional
    public void addComment(long bookId) {
        System.out.println("Добавление комментария");
        Book book = bookRepositoryJpa.findById(bookId).orElseThrow(IllegalArgumentException::new);
        Comment comment = new Comment();
        comment.setBookId(bookId);
        System.out.print("Текст: ");
        comment.setText(in.nextLine());
        book.getComments().add(comment);
    }

    @Override
    @ShellMethod(value = "Delete comment", key = {"delete comment", "bcd"})
    @Transactional
    public void deleteComment(long bookId) {
        Book book = bookRepositoryJpa.findById(bookId).orElseThrow(IllegalArgumentException::new);
        System.out.println("Удаление комментария");
        int i=1;
        for (Comment c: book.getComments()) {
            System.out.println("#"+i+" "+c.getText());
        }
        System.out.print("#: ");
        book.getComments().remove(Integer.parseInt(in.nextLine())-1);
    }

    @Override
    @ShellMethod(value = "All books", key = {"all books", "bga"})
    public void all() {
        System.out.println("Все книги");
        bookRepositoryJpa.findAll().forEach(System.out::println);
    }

    @Override
    @ShellMethod(value = "Find books by title", key = {"find books", "bf"})
    public void findByTitle(String title) {
        System.out.println("Поиск книги "+title);
        System.out.println(bookRepositoryJpa.findByTitle(title));
    }
}

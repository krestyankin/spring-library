package ru.krestyankin.library.service;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;
import ru.krestyankin.library.repositories.AuthorRepository;
import ru.krestyankin.library.repositories.BookRepository;
import ru.krestyankin.library.repositories.CommentRepository;
import ru.krestyankin.library.repositories.GenreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@ShellComponent
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;
    private final Scanner in;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.commentRepository = commentRepository;
        this.in = new Scanner(System.in);
    }

    @Override
    @ShellMethod(value = "Get book", key = {"get book", "bg"})
    public void getById(String bookId) {
        Book book = bookRepository.findById(bookId).get();
        System.out.println("Книга: ");
        System.out.println(book);
        System.out.println("Комментарии: ");
        List<Comment> comments = commentRepository.getCommentsByBook(bookId);
        if(comments.size()!=0) {
            comments.forEach(System.out::println);
        }
        else
            System.out.println("Комментарии отсутствуют");
    }

    @Override
    @ShellMethod(value = "Add book", key = {"add book", "ba"})
    public void add() {
        System.out.println("Добавление книги");
        Book book = new Book();
        System.out.print("Название: ");
        book.setTitle(in.nextLine());
        System.out.print("Автор(ы), ИД через запятую: ");
        book.setAuthors(Arrays.stream(in.nextLine().split("[, ]+")).map(authorId -> authorRepository.findById(authorId).orElseThrow(IllegalArgumentException::new)).collect(Collectors.toList()));
        System.out.print("Жанр(ы), ИД через запятую: ");
        book.setGenres(Arrays.stream(in.nextLine().split("[, ]+")).map(genreId -> genreRepository.findById(genreId).orElseThrow(IllegalArgumentException::new)).collect(Collectors.toList()));
        bookRepository.save(book);
    }

    @Override
    @ShellMethod(value = "Edit book", key = {"edit book", "be"})
    public void update(String bookId) {
        System.out.println("Редактирование книги");
        Book book = bookRepository.findById(bookId).orElseThrow(IllegalArgumentException::new);
        System.out.print("Название: ");
        book.setTitle(in.nextLine());
        System.out.print("Автор(ы), ИД через запятую: ");
        book.setAuthors(Arrays.stream(in.nextLine().split("[, ]+")).map(authorId -> authorRepository.findById(authorId).orElseThrow(IllegalArgumentException::new)).collect(Collectors.toList()));
        System.out.print("Жанр(ы), ИД через запятую: ");
        book.setGenres(Arrays.stream(in.nextLine().split("[, ]+")).map(genreId -> genreRepository.findById(genreId).orElseThrow(IllegalArgumentException::new)).collect(Collectors.toList()));
        bookRepository.save(book);
    }

    @Override
    @ShellMethod(value = "Delete book", key = {"delete book", "bd"})
    public void delete(String bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    @ShellMethod(value = "All books", key = {"all books", "bga"})
    public void all() {
        System.out.println("Все книги");
        bookRepository.findAll().forEach(System.out::println);
    }

    @Override
    @ShellMethod(value = "Find books by title", key = {"find books by title", "bft"})
    public void findByTitle(String title) {
        System.out.println("Поиск книги "+title);
        System.out.println(bookRepository.findByTitleIsLike(title));
    }

    @Override
    @ShellMethod(value = "Find books by author", key = {"find books by author", "bfa"})
    public void findByAuthor() {
        System.out.println("Поиск книги по автору");
        authorRepository.findAll().forEach(author -> System.out.println(author));
        System.out.print("ИД автора: ");
        System.out.println(bookRepository.findByAuthors(authorRepository.findById(in.nextLine()).get()));
    }
}

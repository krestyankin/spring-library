package ru.krestyankin.library.service;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;
import ru.krestyankin.library.repositories.BookRepository;
import ru.krestyankin.library.repositories.CommentRepository;

import java.util.List;
import java.util.Scanner;

@ShellComponent
public class CommentServiceImpl implements CommentService {
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final Scanner in;

    public CommentServiceImpl(BookRepository bookRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
        this.in = new Scanner(System.in);
    }

    @Override
    @ShellMethod(value = "Add comment", key = {"add comment", "ca"})
    public void addComment(String bookId) {
        System.out.println("Добавление комментария");
        Book book = bookRepository.findById(bookId).orElseThrow(IllegalArgumentException::new);
        Comment comment = new Comment();
        comment.setBook(book);
        System.out.print("Текст: ");
        comment.setText(in.nextLine());
        comment.setBook(book);
        commentRepository.save(comment);
    }

    @Override
    @ShellMethod(value = "Delete comment", key = {"delete comment", "cd"})
    public void deleteComment(String bookId) {
        List<Comment> comments = commentRepository.getCommentsByBook(bookId);
        System.out.println("Удаление комментария");
        int i=1;
        for (Comment c: comments) {
            System.out.println("#"+(i++)+" "+c.getText());
        }
        System.out.print("#: ");
        commentRepository.delete(comments.get(Integer.parseInt(in.nextLine())-1));
    }
}

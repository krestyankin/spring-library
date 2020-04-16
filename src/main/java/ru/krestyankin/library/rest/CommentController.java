package ru.krestyankin.library.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;
import ru.krestyankin.library.repositories.BookRepository;
import ru.krestyankin.library.repositories.CommentRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/comment/get/{bookId}")
    public List<Comment> getAll(@PathVariable("bookId") String bookId) {
        return commentRepository.findAllByBook(bookId);
    }

    @PostMapping("/comment/add")
    public @ResponseBody Comment add(@RequestParam("bookId") String bookId,
                                     @RequestParam("text")   String text) {
        Book book = bookRepository.findById(bookId).orElseThrow(NotFoundException::new);
        return commentRepository.save(new Comment(text, book));
    }

    @DeleteMapping("/comment/delete/{commentId}")
    public void delete(@PathVariable("commentId") String id) {
        commentRepository.deleteById(id);
    }
}

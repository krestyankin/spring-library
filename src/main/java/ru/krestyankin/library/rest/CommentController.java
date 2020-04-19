package ru.krestyankin.library.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.krestyankin.library.models.Comment;
import ru.krestyankin.library.repositories.BookRepository;
import ru.krestyankin.library.repositories.CommentRepository;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @PostMapping("/comment/save")
    public String savePage(@RequestParam("bookId") String bookId,
                           @RequestParam("text")   String text) {
        commentRepository.save(new Comment(text, bookRepository.findById(bookId).orElseThrow(NotFoundException::new)));
        return "comment/save";
    }

    @GetMapping("/comment/delete")
    public String deletePage(@RequestParam("id") String id) {
        commentRepository.deleteById(id);
        return "comment/delete";
    }
}

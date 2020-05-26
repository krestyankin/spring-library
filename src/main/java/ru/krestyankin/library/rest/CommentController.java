package ru.krestyankin.library.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.krestyankin.library.models.Comment;
import ru.krestyankin.library.service.BookService;
import ru.krestyankin.library.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final BookService bookService;
    private final CommentService commentService;

    @PostMapping("/comment/save")
    public String savePage(@RequestParam("bookId") String bookId,
                           @RequestParam("text")   String text) {
        commentService.save(new Comment(text, bookService.findById(bookId)));
        return "comment/save";
    }

    @GetMapping("/comment/delete")
    public String deletePage(@RequestParam("id") String id) {
        commentService.deleteById(id);
        return "comment/delete";
    }
}

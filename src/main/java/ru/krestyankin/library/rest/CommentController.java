package ru.krestyankin.library.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.krestyankin.library.models.Comment;
import ru.krestyankin.library.repositories.BookRepository;
import ru.krestyankin.library.repositories.CommentRepository;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/comment/get/{bookId}")
    public Flux<Comment> getAll(@PathVariable("bookId") String bookId) {
        return commentRepository.findAllByBook(bookId);
    }

    @PostMapping("/comment/add")
    public @ResponseBody Mono<Comment> add(ServerWebExchange exchange) {
        return exchange.getFormData().flatMap(value -> {
            return bookRepository.findById(value.getFirst("bookId")).flatMap(book -> commentRepository.save(new Comment(value.getFirst("text"), book)));
        });
    }

    @DeleteMapping("/comment/delete/{commentId}")
    public Mono<Void> delete(@PathVariable("commentId") String id) {
        return commentRepository.deleteById(id);
    }
}

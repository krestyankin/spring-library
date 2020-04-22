package ru.krestyankin.library.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorRepository authorRepository;

    @GetMapping("/author/all")
    public Flux<Author> getAll() {
        return authorRepository.findAll();
    }
}

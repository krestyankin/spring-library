package ru.krestyankin.library.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.krestyankin.library.models.Genre;
import ru.krestyankin.library.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final GenreRepository genreRepository;

    @GetMapping("/genre/all")
    public Flux<Genre> getAll() {
        return genreRepository.findAll();
    }
}

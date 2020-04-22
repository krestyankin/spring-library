package ru.krestyankin.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.BookDto;
import ru.krestyankin.library.models.Genre;
import ru.krestyankin.library.repositories.AuthorRepository;
import ru.krestyankin.library.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookDtoConverter {
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookDto convertToDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                    book.getAuthors().stream().map(a->a.getId()).collect(Collectors.toList()),
                    book.getGenres().stream().map(g->g.getId()).collect(Collectors.toList())
            );
    }

    public Mono<Book> toDomainObject(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        Mono<List<Author>> authors = authorRepository.findAllById(bookDto.getAuthors()).collectList();
        Mono<List<Genre>> genres = genreRepository.findAllById(bookDto.getGenres()).collectList();
        return Mono.zip(Mono.just(book), authors, genres).map(tuple -> {
            tuple.getT1().setAuthors(tuple.getT2());
            tuple.getT1().setGenres(tuple.getT3());
            return tuple.getT1();
        });
    }
}

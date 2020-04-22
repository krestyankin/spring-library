package ru.krestyankin.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.BookDto;
import ru.krestyankin.library.repositories.AuthorRepository;
import ru.krestyankin.library.repositories.GenreRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public Book toDomainObject(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setAuthors(StreamSupport.stream(authorRepository.findAllById(bookDto.getAuthors()).spliterator(),false).collect(Collectors.toList()));
        book.setGenres(StreamSupport.stream(genreRepository.findAllById(bookDto.getGenres()).spliterator(),false).collect(Collectors.toList()));
        return book;
    }
}

package ru.krestyankin.library.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.krestyankin.library.models.BookDto;
import ru.krestyankin.library.repositories.AuthorRepository;
import ru.krestyankin.library.repositories.BookRepository;
import ru.krestyankin.library.repositories.GenreRepository;
import ru.krestyankin.library.service.BookDtoConverter;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookDtoConverter bookDtoConverter;

    @GetMapping(value = "/book/list", produces = "application/json")
    public @ResponseBody List<BookDto> getAll() {
        return  bookRepository.findAll().stream().map(bookDtoConverter::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/book/view")
    public String viewPage(@RequestParam("id") String id, Model model) {
        model.addAttribute("book",  bookRepository.findById(id).orElseThrow(NotFoundException::new));
        return "book/view";
    }

    @GetMapping("/book/edit")
    public String editPage(@RequestParam("id") String id, Model model) {
        model.addAttribute("book",  bookDtoConverter.convertToDto(bookRepository.findById(id).orElseThrow(NotFoundException::new)));
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "book/edit";
    }

    @GetMapping("/book/add")
    public String addPage( Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "book/add";
    }

    @PostMapping("/book/save")
    public String savePage(BookDto bookDto) {
        bookRepository.save(bookDtoConverter.toDomainObject(bookDto));
        return "book/save";
    }

    @GetMapping("/book/delete")
    public String deletePage(@RequestParam("id") String id, Model model) {
        bookRepository.deleteById(id);
        return "book/delete";
    }
}

package ru.krestyankin.library.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import ru.krestyankin.library.models.BookDto;
import ru.krestyankin.library.repositories.BookRepository;
import ru.krestyankin.library.service.BookDtoConverter;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    private final BookDtoConverter bookDtoConverter;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping(value = "/book/list", produces = "application/json")
    public @ResponseBody Flux<BookDto> getAll() {
        return bookRepository.findAll().map(bookDtoConverter::convertToDto);
    }

    @GetMapping("/book/view")
    public String viewPage(@RequestParam("id") String id, Model model) {
        model.addAttribute("book",  bookRepository.findById(id));
        return "book/view";
    }

    @GetMapping("/book/edit")
    public String editPage(@RequestParam("id") String id, Model model) {
        model.addAttribute("book",  bookRepository.findById(id).map(bookDtoConverter::convertToDto));;
        return "book/edit";
    }

    @GetMapping("/book/add")
    public String addPage() {
        return "book/add";
    }

    @PostMapping("/book/save")
    public String savePage(BookDto bookDto, Model model) {
        model.addAttribute("book", bookDtoConverter.toDomainObject(bookDto).flatMap(bookRepository::save));
        return "book/save";
    }

    @GetMapping("/book/delete")
    public String deletePage(@RequestParam("id") String id, Model model) {
        model.addAttribute("book", bookRepository.deleteById(id));
        return "book/delete";
    }
}

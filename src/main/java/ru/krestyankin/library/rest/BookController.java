package ru.krestyankin.library.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.BookDto;
import ru.krestyankin.library.repositories.AuthorRepository;
import ru.krestyankin.library.repositories.BookRepository;
import ru.krestyankin.library.repositories.CommentRepository;
import ru.krestyankin.library.repositories.GenreRepository;
import ru.krestyankin.library.security.LibraryUserPrincipal;
import ru.krestyankin.library.service.BookDtoConverter;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookDtoConverter bookDtoConverter;

    @GetMapping("/")
    public String listPage(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        LibraryUserPrincipal user = (LibraryUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("loggedIn", (user.getUsername()!=null));
        return "book/list";
    }

    @GetMapping("/book/view")
    public String viewPage(@RequestParam("id") String id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book",  book);
        model.addAttribute("comments", commentRepository.getCommentsByBook(id));
        return "book/view";
    }

    @GetMapping("/book/edit")
    public String editPage(@RequestParam("id") String id, Model model) {
        Book book = bookRepository.findById(id).orElseThrow(NotFoundException::new);
        model.addAttribute("book",  bookDtoConverter.convertToDto(book));
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

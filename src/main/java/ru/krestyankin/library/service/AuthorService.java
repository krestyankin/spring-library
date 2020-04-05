package ru.krestyankin.library.service;

import ru.krestyankin.library.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
   Optional<Author> getById(String authorId);
   void add();
   void update(String authorId);
   void delete(String authorId);
   List<Author> getAll();
   void count();
}

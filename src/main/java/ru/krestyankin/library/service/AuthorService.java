package ru.krestyankin.library.service;

import ru.krestyankin.library.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
   Optional<Author> getById(long authorId);
   void add();
   void update(long authorId);
   void delete(long authorId);
   List<Author> getAll();
   void count();
}

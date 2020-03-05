package ru.krestyankin.library.service;

import ru.krestyankin.library.domain.Author;

public interface AuthorService {
   Author getById(long authorId);
   void add();
   void update(long authorId);
   void delete(long authorId);
}

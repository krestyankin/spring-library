package ru.krestyankin.library.service;

import ru.krestyankin.library.models.Genre;

import java.util.List;

public interface GenreService {
   void add();
   List<Genre> getAll();
}

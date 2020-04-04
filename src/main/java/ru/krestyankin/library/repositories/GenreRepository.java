package ru.krestyankin.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.krestyankin.library.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}

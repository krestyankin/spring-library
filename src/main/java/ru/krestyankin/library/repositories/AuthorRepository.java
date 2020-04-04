package ru.krestyankin.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.krestyankin.library.models.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByFullname(String fullname);

}

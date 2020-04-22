package ru.krestyankin.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.krestyankin.library.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

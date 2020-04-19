package ru.krestyankin.library.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.krestyankin.library.models.User;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}

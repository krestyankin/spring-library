package ru.krestyankin.library.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.krestyankin.library.models.User;
import ru.krestyankin.library.repositories.UserRepository;
import ru.krestyankin.library.security.LibraryUserPrincipal;

@Service
public class LibraryUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public LibraryUserDetailsService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new LibraryUserPrincipal(user);
    }
}
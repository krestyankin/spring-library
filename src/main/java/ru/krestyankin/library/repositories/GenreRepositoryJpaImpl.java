package ru.krestyankin.library.repositories;

import org.springframework.stereotype.Repository;
import ru.krestyankin.library.models.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryJpaImpl implements GenreRepositoryJpa {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() <= 0){
            em.persist(genre);
            return genre;
        }
        else {
            return em.merge(genre);
        }
    }

    @Override
    public Optional<Genre> findById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public List<Genre> findAll() {
        return em.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.find(Genre.class, id));
    }

    @Override
    public long count() {
        return em.createQuery(
                "select count(g) from Genre g", Long.class).getSingleResult();

    }
}

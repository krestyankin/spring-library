package ru.krestyankin.library.repositories;

import org.springframework.stereotype.Repository;
import ru.krestyankin.library.models.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryJpaImpl implements AuthorRepositoryJpa {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Author save(Author author) {
        if (author.getId() <= 0){
            em.persist(author);
            return author;
        }
        else {
            return em.merge(author);
        }
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public List<Author> findAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public List<Author> findByFullname(String fullname) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.fullname = :fullname", Author.class);
        query.setParameter("fullname", fullname);
        return query.getResultList();
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.find(Author.class, id));
    }

    @Override
    public long count() {
        return em.createQuery(
                "select count(a) from Author a", Long.class).getSingleResult();

    }
}

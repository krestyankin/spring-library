package ru.krestyankin.library.repositories;

import org.springframework.stereotype.Repository;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.models.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryJpaImpl implements BookRepositoryJpa {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Book save(Book book) {
        if (book.getId() <= 0){
            em.persist(book);
            return book;
        }
        else {
            return em.merge(book);
        }
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        TypedQuery<Book> query = em.createQuery("select distinct b from Book b join fetch b.authors  join fetch b.genres where b.title like :title", Book.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

    @Override
    public List<Book> findByAuthor(Author author) {
        return author.getBooks();
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.find(Book.class, id));
    }

    @Override
    public long count() {
        return em.createQuery("select count(b) from Book b", Long.class).getSingleResult();
    }
}

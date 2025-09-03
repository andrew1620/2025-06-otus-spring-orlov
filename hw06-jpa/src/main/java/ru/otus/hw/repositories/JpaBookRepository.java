package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    private final String authorsGenresEntityGraphName = "books-authors-genres-entity-graph";

    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph(authorsGenresEntityGraphName);
        return Optional.ofNullable(em.find(Book.class, id, Map.of(FETCH.getKey(), entityGraph)));
    }

    private Book findByIdOrThrow(long id) {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE id = :id", Book.class);
        query.setParameter("id", id);
        return query.getResultList().stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d doesn't exist".formatted(id)));
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);
        EntityGraph<?> entityGraph = em.getEntityGraph(authorsGenresEntityGraphName);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        Book book = findByIdOrThrow(id);
        em.remove(book);
    }

    private Book insert(Book book) {
        em.persist(book);
        return book;
    }

    private Book update(Book book) {
        findByIdOrThrow(book.getId());
        em.merge(book);
        return book;
    }

}

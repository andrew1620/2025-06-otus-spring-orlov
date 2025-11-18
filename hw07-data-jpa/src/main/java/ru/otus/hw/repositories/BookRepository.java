package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = "books-authors-genres-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Book> findById(long id);

    @Override
    @EntityGraph(value = "books-authors-genres-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    List<Book> findAll();
}

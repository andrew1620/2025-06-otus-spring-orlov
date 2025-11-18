package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(value = "comments-books-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Comment> findById(long id);

    @Query(value = """
            SELECT c FROM Comment c
                        JOIN FETCH c.book b WHERE c.book.id = :bookId""")
    List<Comment> findByBookId(@Param("bookId") long bookId);
}

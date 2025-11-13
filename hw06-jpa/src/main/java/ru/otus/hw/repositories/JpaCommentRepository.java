package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;


@RequiredArgsConstructor
@Repository
public class JpaCommentRepository implements CommentRepository {

    private final String bookEntityGraphName = "comments-books-entity-graph";

    private final EntityManager em;

    @Override
    public Optional<Comment> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph(bookEntityGraphName);
        return Optional.ofNullable(em.find(Comment.class, id, Map.of(FETCH.getKey(), entityGraph)));
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            em.persist(comment);
            return comment;
        }
        return em.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(em::remove);
    }

    @Override
    public List<Comment> findByBookId(long bookId) {
        TypedQuery<Comment> query = em.createQuery("""
                SELECT c FROM Comment c JOIN FETCH c.book JOIN FETCH c.book.genres JOIN FETCH c.book.author
                WHERE c.book.id = :bookId""", Comment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

}

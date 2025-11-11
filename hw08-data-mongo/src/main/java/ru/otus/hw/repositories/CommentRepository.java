package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends MongoRepository<Comment, String> {
    Optional<Comment> findById(String id);

    List<Comment> findByBookId(@Param("bookId") String bookId);
}

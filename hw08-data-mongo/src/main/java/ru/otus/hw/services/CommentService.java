package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(String id);

    Comment insert(String title, String bookId);

    Comment update(String id, String title, String bookId);

    void deleteById(String id);

    List<Comment> findByBookId(String bookId);
}

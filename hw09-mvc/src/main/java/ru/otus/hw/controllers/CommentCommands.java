package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    // fcbid 4
    public String findCommentById(long id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %d not found".formatted(id));
    }

    // cbbid 1
    public String findCommentsByBookId(long id) {
        return commentService.findByBookId(id).stream().map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // cins newComment 1
    public String insertBook(String title, long bookId) {
        var savedBook = commentService.insert(title, bookId);
        return commentConverter.commentToString(savedBook);
    }

    // cupd 4 editedComment 3
    public String updateBook(long id, String title, long bookId) {
        var savedBook = commentService.update(id, title, bookId);
        return commentConverter.commentToString(savedBook);
    }

    // cdel 4
    public void deleteBook(long id) {
        commentService.deleteById(id);
    }
}

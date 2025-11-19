package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    // cbid 4
    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(String id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %s not found".formatted(id));
    }

    // cbbid 1
    @ShellMethod(value = "Find comments by bookId", key = "cbbid")
    public String findCommentsByBookId(String id) {
        return commentService.findByBookId(id).stream().map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    // cins newComment 1
    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertBook(String title, String bookId) {
        var savedBook = commentService.insert(title, bookId);
        return commentConverter.commentToString(savedBook);
    }

    // cupd 4 editedComment 3
    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateBook(String id, String title, String bookId) {
        var savedBook = commentService.update(id, title, bookId);
        return commentConverter.commentToString(savedBook);
    }

    // cdel 4
    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteBook(String id) {
        commentService.deleteById(id);
    }
}

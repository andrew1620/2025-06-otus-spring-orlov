package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;


    @GetMapping("books/{id}/comments")
    public List<Comment> findCommentsByBookId(@PathVariable("id") long id) {
        return commentService.findByBookId(id);
    }

}

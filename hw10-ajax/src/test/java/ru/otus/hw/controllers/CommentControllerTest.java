package ru.otus.hw.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.rest.CommentController;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.CommentService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    void findCommentsByBookId_ShouldReturnComments() throws Exception {
        Book book = new Book(1L, "Test Book", new Author(1L, "Author"), null);
        List<Comment> comments = List.of(
                new Comment(1L, "Great book", book),
                new Comment(2L, "Interesting read", book)
        );

        given(commentService.findByBookId(1L)).willReturn(comments);

        mockMvc.perform(get("/comments/by-book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Great book"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Interesting read"));
    }

    @Test
    void findCommentsByBookId_WhenNoComments_ShouldReturnEmptyList() throws Exception {
        given(commentService.findByBookId(1L)).willReturn(List.of());

        mockMvc.perform(get("/comments/by-book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

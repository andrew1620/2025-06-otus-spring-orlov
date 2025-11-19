package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.rest.BookController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @Test
    void getBooks_ShouldReturnListOfBooks() throws Exception {
        Author author = new Author(1L, "Test Author");
        Genre genre = new Genre(1L, "Test Genre");
        Book book = new Book(1L, "Test Book", author, List.of(genre));

        given(bookService.findAll()).willReturn(List.of(book));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    @Test
    void findBookById_ShouldReturnBook() throws Exception {
        Author author = new Author(1L, "Test Author");
        Genre genre = new Genre(1L, "Test Genre");
        Book book = new Book(1L, "Test Book", author, List.of(genre));
        List<Comment> comments = List.of(new Comment(1L, "Great book", book));

        given(bookService.findByIdThrowing(1L)).willReturn(book);
        given(commentService.findByBookId(1L)).willReturn(comments);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author.id").value(1))
                .andExpect(jsonPath("$.author.fullName").value("Test Author"));
    }

    @Test
    void insertBook_ShouldCreateNewBook() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("New Book");
        bookDto.setAuthorId(1L);
        bookDto.setGenreIdList(Set.of(1L, 2L));

        Author author = new Author(1L, "Test Author");
        Book savedBook = new Book(1L, "New Book", author, List.of());

        given(bookService.insert(anyString(), anyLong(), any())).willReturn(savedBook);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "New Book",
                                "authorId": 1,
                                "genreIdList": [1, 2]
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Book"));

        verify(bookService).insert("New Book", 1L, Set.of(1L, 2L));
    }

    @Test
    void editBook_ShouldUpdateBook() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Updated Book");
        bookDto.setAuthorId(1L);
        bookDto.setGenreIdList(Set.of(1L, 2L));

        Author author = new Author(1L, "Test Author");
        Book updatedBook = new Book(1L, "Updated Book", author, List.of());

        given(bookService.update(anyLong(), anyString(), anyLong(), any())).willReturn(updatedBook);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "Updated Book",
                                "authorId": 1,
                                "genreIdList": [1, 2]
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Book"));

        verify(bookService).update(1L, "Updated Book", 1L, Set.of(1L, 2L));
    }

    @Test
    void deleteBook_ShouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());

        verify(bookService).deleteById(1L);
    }
}
package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @Test
    void findBookById_ShouldReturnBookViewWithAttributes() throws Exception {
        Book book = new Book(1L, "Test Book", new Author(1L, "Author"), List.of(new Genre(1L, "Genre")));
        given(bookService.findById(1L)).willReturn(Optional.of(book));
        given(commentService.findByBookId(1L)).willReturn(List.of());

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    void insertBook_ShouldRedirectToHome() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("New Book");
        bookDto.setAuthorId(1L);
        bookDto.setGenreIdList(Set.of(1L, 2L));

        Book savedBook = new Book(1L, "New Book", null, null);
        given(bookService.insert(anyString(), anyLong(), any())).willReturn(savedBook);

        mockMvc.perform(post("/books")
                        .param("title", "New Book")
                        .param("authorId", "1")
                        .param("genreIdList", "1", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(bookService).insert("New Book", 1L, Set.of(1L, 2L));
    }

    @Test
    void saveBook_ShouldReturnSaveViewWithAttributes() throws Exception {
        Author author = new Author(1L, "Test Author");
        Genre genre = new Genre(1L, "Test Genre");

        given(authorService.findAll()).willReturn(List.of(author));
        given(genreService.findAll()).willReturn(List.of(genre));

        mockMvc.perform(get("/books/save"))
                .andExpect(status().isOk())
                .andExpect(view().name("save"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void editBook_ShouldReturnEditViewWithAttributes() throws Exception {
        Book book = new Book(1L, "Test Book", new Author(1L, "Author"), List.of(new Genre(1L, "Genre")));
        Author author = new Author(1L, "Test Author");
        Genre genre = new Genre(1L, "Test Genre");

        given(bookService.findById(1L)).willReturn(Optional.of(book));
        given(authorService.findAll()).willReturn(List.of(author));
        given(genreService.findAll()).willReturn(List.of(genre));

        mockMvc.perform(get("/books/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    void updateBook_ShouldRedirectToHome() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Updated Book");
        bookDto.setAuthorId(1L);
        bookDto.setGenreIdList(Set.of(1L, 2L));

        Book updatedBook = new Book(1L, "Updated Book", null, null);
        given(bookService.update(anyLong(), anyString(), anyLong(), any())).willReturn(updatedBook);

        mockMvc.perform(put("/books")
                        .param("id", "1")
                        .param("title", "Updated Book")
                        .param("authorId", "1")
                        .param("genreIdList", "1", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(bookService).update(1L, "Updated Book", 1L, Set.of(1L, 2L));
    }

    @Test
    void deleteBook_ShouldRedirectToHome() throws Exception {
        mockMvc.perform(post("/books/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(bookService).deleteById(1L);
    }
}
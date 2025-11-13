package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

import java.util.List;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    private final CommentService commentService;


    @GetMapping("books")
    public List<Book> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("books/{id}")
    public Book findBookById(Model model, @PathVariable("id") long id) {
        Book book = bookService.findById(id).get();
        List<Comment> comments = commentService.findByBookId(book.getId());
        return book;
    }

    @PostMapping("books")
    public Book insertBook(@RequestBody BookDto dto) {
        return bookService.insert(dto.getTitle(), dto.getAuthorId(), dto.getGenreIdList());
    }


    @PutMapping("books/{id}")
    public Book editBook(@PathVariable("id") long id, @RequestBody BookDto dto) {
        return bookService.update(id, dto.getTitle(), dto.getAuthorId(), dto.getGenreIdList());
    }

    @DeleteMapping("books/{id}")
    public void deleteBook(@PathVariable("id") long id) {
        bookService.deleteById(id);
    }
}

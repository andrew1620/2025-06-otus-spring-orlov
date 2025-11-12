package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;


    @GetMapping("books/{id}")
    public String findBookById(Model model, @PathVariable("id") long id) {
        Book book = bookService.findById(id).get();
        List<Comment> comments = commentService.findByBookId(book.getId());
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "book";
    }

    @PostMapping("books")
    public String insertBook(@ModelAttribute("book") BookDto dto) {
        var savedBook = bookService.insert(dto.getTitle(), dto.getAuthorId(), dto.getGenreIdList());
        return "redirect:/home";
    }

    @GetMapping("books/save")
    public String saveBook(Model model) {

        List<Author> authors = authorService.findAll();
        List<Genre> genres = genreService.findAll();

        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);

        return "save";
    }

    @GetMapping("books/edit")
    public String editBook(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id).get();

        List<Author> authors = authorService.findAll();
        List<Genre> genres = genreService.findAll();

        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "edit";
    }

    @PutMapping("books")
    public String updateBook(@ModelAttribute("book") BookDto dto) {
        var savedBook = bookService.update(dto.getId(), dto.getTitle(), dto.getAuthorId(), dto.getGenreIdList());
        return "redirect:/home";
    }

    @DeleteMapping("books")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/home";
    }
}

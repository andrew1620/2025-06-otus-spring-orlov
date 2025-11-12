package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;


    @RequestMapping("home")
    public String findAll(Model model) {
        List<Book> books = bookService.findAll();
        List<Author> authors = authorService.findAll();
        List<Genre> genres = genreService.findAll();
        model.addAllAttributes(Map.of("books", books, "authors", authors, "genres", genres));
        return "home";
    }
}

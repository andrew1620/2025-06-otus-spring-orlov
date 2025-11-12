package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @RequestMapping("authors")
    public String findAllAuthors(Model model) {
        List<Author> items = authorService.findAll();
        model.addAttribute("authors", items);
        return "authors";
    }

    public String findById(long id) {
        return authorService.findById(id).stream()
                .map(authorConverter::authorToString).findFirst().get();
    }
}

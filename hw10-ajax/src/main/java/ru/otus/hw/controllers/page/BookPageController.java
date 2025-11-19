package ru.otus.hw.controllers.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@Controller
public class BookPageController {

    @GetMapping("book/{bookId}")
    public String book() {
        return "book";
    }

    @GetMapping("book/edit/{bookId}")
    public String editBook() {
        return "edit";
    }

    @GetMapping("book/create")
    public String createBook() {
        return "create";
    }

}

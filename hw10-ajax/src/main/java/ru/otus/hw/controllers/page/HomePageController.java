package ru.otus.hw.controllers.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@Controller
public class HomePageController {

    @RequestMapping("home")
    public String getHome() {
        return "home";
    }

}

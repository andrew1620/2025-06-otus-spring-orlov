package ru.otus.hw.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.otus.hw.exceptions.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handeNotFoundException(EntityNotFoundException ex) {
        return new ModelAndView("error",
                "errorText", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handeNotFoundException(IllegalArgumentException ex) {
        return new ModelAndView("error",
                "errorText", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handeNotFoundException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ModelAndView("error",
                "errorText", "Sorry, an error occurred.");
    }
}

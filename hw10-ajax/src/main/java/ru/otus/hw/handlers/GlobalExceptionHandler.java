package ru.otus.hw.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.exceptions.ExceptionResponse;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handeNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(
                new ExceptionResponse().setMessage(ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handeNotFoundException(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                new ExceptionResponse().setMessage(ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handeNotFoundException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ExceptionResponse().setMessage("Sorry, an error occurred."),
                HttpStatus.BAD_REQUEST);
    }
}

package ru.otus.hw.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
public class ExceptionResponse {
    private String message;
}

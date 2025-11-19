package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookDto {
    private Long id;

    private String title;

    private Long authorId;

    private Set<Long> genreIdList;
}

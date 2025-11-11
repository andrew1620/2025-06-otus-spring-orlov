package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {
    private final BookConverter bookConverter;

    public String commentToString(Comment comment) {
        return "Id: %s, title: %s, book: {%s}".formatted(
                comment.getId(),
                comment.getTitle(),
                bookConverter.bookToString(comment.getBook()));
    }
}

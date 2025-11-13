package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataMongoTest
@Import({CommentServiceImpl.class, BookServiceImpl.class})
public class CommentServiceIntegrationTest {
    @Autowired
    private CommentService commentService;

    @DisplayName("Не должен выбрасываться LazyInitializationException при загрузке всех комментов по id книги")
    @Test
    void shouldNotThrowLazyExceptionWhenFindByBookId() {
        var comments = commentService.findByBookId("1");
        assertThat(comments).isNotEmpty();

        comments.forEach( comment -> {
            assertThatCode(() -> {
                assertThat(comment.getBook().getAuthor()).isNotNull();
                assertThat(comment.getBook().getGenres()).isNotNull();
            }).doesNotThrowAnyException();
        });
    }

    @DisplayName("Не должен выбрасываться LazyInitializationException при загрузке коммента по id")
    @Test
    void shouldNotThrowLazyExceptionWhenFindById() {
        var comment = commentService.findById("1").orElse(null);
            assertThat(comment).isNotNull();
            assertThatCode(() -> {
                assertThat(comment.getBook().getAuthor()).isNotNull();
                assertThat(comment.getBook().getGenres()).isNotNull();
            }).doesNotThrowAnyException();
    }
}

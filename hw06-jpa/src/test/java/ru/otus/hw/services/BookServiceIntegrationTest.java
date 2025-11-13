package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.repositories.JpaAuthorRepository;
import ru.otus.hw.repositories.JpaBookRepository;
import ru.otus.hw.repositories.JpaGenreRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataJpaTest
@Import({BookServiceImpl.class, JpaAuthorRepository.class, JpaGenreRepository.class, JpaBookRepository.class})
public class BookServiceIntegrationTest {
    @Autowired
    private BookService bookService;

    @DisplayName("Не должен выбрасываться LazyInitializationException при загрузке всех книг")
    @Test
    void shouldNotThrowLazyExceptionWhenFindAll() {
        var books = bookService.findAll();
        assertThat(books).isNotEmpty();

        books.forEach(book -> {
            assertThatCode(() -> {
                assertThat(book.getAuthor()).isNotNull();
                assertThat(book.getGenres()).isNotNull();
            }).doesNotThrowAnyException();
        });
    }

    @DisplayName("Не должен выбрасываться LazyInitializationException при загрузке книги по id")
    @Test
    void shouldNotThrowLazyExceptionWhenFindById() {
        var book = bookService.findById(1L).orElse(null);
        assertThat(book).isNotNull();
        assertThatCode(() -> {
            assertThat(book.getAuthor()).isNotNull();
            assertThat(book.getGenres()).isNotNull();
        }).doesNotThrowAnyException();
    }
}

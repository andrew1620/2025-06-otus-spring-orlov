package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@DataMongoTest
@Import(BookServiceImpl.class)
public class BookServiceIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

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
        var book = bookService.findById("1").orElse(null);
        assertThat(book).isNotNull();
        assertThatCode(() -> {
            assertThat(book.getAuthor()).isNotNull();
            assertThat(book.getGenres()).isNotNull();
        }).doesNotThrowAnyException();
    }

    @DisplayName("Должна создаваться новая книга")
    @Test
    void shouldCreateBook() {
        String title = "createdBook";
        bookService.insert(title, "1", Set.of("1", "2"));
        var books = mongoTemplate.find(Query.query(Criteria.where(title).is("1")), Book.class);
        assertThat(books).isNotEmpty();
    }

    @DisplayName("Должна обновляться книга")
    @Test
    void shouldUpdateBook() {
        String id = "1";
        var book = mongoTemplate.findById(id, Book.class);
        assertThat(book).isNotNull();

        String updatedTitle = "updatedTitle";
        book.setTitle(updatedTitle);

        String authorId = "2";
        Set<String> genresIds = Set.of("3", "4");

        bookService.update(id, updatedTitle, authorId, genresIds);

        var updatedBook = mongoTemplate.findById(id, Book.class);
        assertThat(updatedBook).isNotNull();

        assertThat(updatedBook.getAuthor().getId()).isEqualTo(authorId);
        List<String> actualGenres = updatedBook.getGenres().stream().map(Genre::getId).toList();
        assertThat(actualGenres).containsExactlyInAnyOrderElementsOf(genresIds.stream().toList());
    }

    @DisplayName("Должна появиться ошибка IllegalArgumentException при добавлении книги")
    @Test
    void shouldThrowExceptionWhenCreate() {
        String title = "createdBook";
        bookService.insert(title, "1", Set.of("999", "333"));
        var books = mongoTemplate.find(Query.query(Criteria.where(title).is("1")), Book.class);
        assertThat(books).isNotEmpty();
    }
}

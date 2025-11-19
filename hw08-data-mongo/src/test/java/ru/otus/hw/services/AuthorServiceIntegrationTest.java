package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@Import(AuthorServiceImpl.class)
class AuthorServiceIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Author.class);

        Author author1 = new Author("1", "Test Author 1");
        Author author2 = new Author("2", "Test Author 2");
        Author author3 = new Author("3", "Test Author 3");

        mongoTemplate.insertAll(List.of(author1, author2, author3));
    }

    @DisplayName("Должен возвращать всех авторов")
    @Test
    void findAll_ShouldReturnAllAuthors() {

        List<Author> authors = authorService.findAll();

        assertThat(authors).isNotEmpty();
        assertThat(authors).hasSizeGreaterThanOrEqualTo(1);
        authors.forEach(author -> {
            assertThat(author.getId()).isNotBlank();
            assertThat(author.getFullName()).isNotBlank();
        });
    }

    @DisplayName("Должен находить автора по существующему ID")
    @Test
    void findById_WithExistingId_ShouldReturnAuthor() {
        String existingId = "1";

        Optional<Author> author = authorService.findById(existingId);

        assertThat(author).isPresent();
        assertThat(author.get().getId()).isEqualTo(existingId);
    }

    @DisplayName("Должен бросать исключение при поиске автора по несуществующему ID")
    @Test
    void findById_WithNonExistingId_ShouldThrowException() {
        String nonExistingId = "777";

        assertThatThrownBy(() -> authorService.findById(nonExistingId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("Должен бросать исключение при поиске автора по null ID")
    @Test
    void findById_WithNullId_ShouldThrowException() {
        String id = "777";

        assertThatThrownBy(() -> authorService.findById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("Должен возвращать пустой список когда нет авторов")
    @Test
    void findAll_WhenNoAuthors_ShouldReturnEmptyList() {
        mongoTemplate.dropCollection(Author.class);

        List<Author> authors = authorService.findAll();

        assertThat(authors).isEmpty();
    }
}

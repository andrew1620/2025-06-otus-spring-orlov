package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import(JpaBookRepository.class)
class JpaBookRepositoryTest {

    private static final long BOOK_ID = 1L;
    private static final long BOOK_AUTHOR_ID = 1L;
    private static final long BOOK_GENRE_ID = 1L;

    @Autowired
    private EntityManager em;

    @Autowired
    private BookRepository repository;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        var actualBook = repository.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var genre = em.find(Genre.class, BOOK_GENRE_ID);
        var author = em.find(Author.class, BOOK_AUTHOR_ID);
        var expectedBook = new Book(0, "BookTitle_10500", author,
                List.of(genre));
        var returnedBook = repository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(repository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var genre = em.find(Genre.class, BOOK_GENRE_ID);
        var author = em.find(Author.class, BOOK_AUTHOR_ID);
        var bookToUpdate = em.find(Book.class, 2L);
        bookToUpdate.setTitle("BookTitle_10500").setAuthor(author).setGenres(new ArrayList<>(List.of(genre)));
        repository.save(bookToUpdate);
        em.detach(bookToUpdate);
        var updatedBook = em.find(Book.class, bookToUpdate.getId());

        assertThat(updatedBook)
                .usingRecursiveComparison()
                .isEqualTo(bookToUpdate);
    }

    @DisplayName("должен выбрасывать EntityNotFoundException при обновлении")
    @Test
    void shouldThrowExceptionNotFoundWhenSaveUpdatedBook() {
        var bookToUpdate = new Book().setId(333L);

        assertThatThrownBy(() -> repository.save(bookToUpdate))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        assertThat(em.find(Book.class, BOOK_ID)).isNotNull();
        repository.deleteById(BOOK_ID);
        assertThat(em.find(Book.class, BOOK_ID)).isNull();
    }

    @DisplayName("должен выбрасывать EntityNotFoundException при удалении")
    @Test
    void shouldThrowExceptionNotFoundWhenDeleteBook() {
        assertThatThrownBy(() -> repository.deleteById(333L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}
package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Data-Jpa для работы с комментариями")
@DataJpaTest
class CommentRepositoryTest {

    private static final long COMMENT_ID = 1L;
    private static final long BOOK_ID = 1L;
    private static final long BOOK_AUTHOR_ID = 1L;
    private static final long BOOK_GENRE_ID = 1L;

    @Autowired
    private EntityManager em;

    @Autowired
    private CommentRepository repository;

    @Autowired
    private BookRepository bookRepository;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbComments")
    void shouldReturnCorrectCommentById(Comment expectedComment) {
        var actualComment = repository.findById(expectedComment.getId());
        assertThat(actualComment).isPresent()
                .get()
                .usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать комментарии по bookId")
    @Test
    void shouldReturnCorrectCommentsByBookId() {
        var expectedComment = em.find(Comment.class, COMMENT_ID);
        var actualComments = repository.findByBookId(BOOK_ID);
        assertThat(actualComments.get(0).getId()).isEqualTo(expectedComment.getId());
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var book = em.find(Book.class, BOOK_ID);
        var commentToCreate = new Comment().setId(0).setBook(book).setTitle("Created_comment");
        var returnedComment = repository.save(commentToCreate);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(commentToCreate);
        var commentFromDb = em.find(Comment.class, returnedComment.getId());
        assertThat(commentFromDb).isEqualTo(commentToCreate);
    }

    @DisplayName("должен сохранять измененный комментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var book = bookRepository.findById(1L).get();
        var commentToUpdate = em.find(Comment.class, 3L);
        commentToUpdate.setTitle("Updated_comment").setBook(book);
        repository.save(commentToUpdate);
        em.flush();
        em.clear();
        var updatedComment = repository.findById(commentToUpdate.getId());

        assertThat(updatedComment.get())
                .usingRecursiveComparison()
                .isEqualTo(commentToUpdate);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertThat(em.find(Comment.class, COMMENT_ID)).isNotNull();
        repository.deleteById(BOOK_ID);
        assertThat(em.find(Comment.class, COMMENT_ID)).isNull();
    }

    private static List<Comment> getDbComments(List<Book> dbBooks) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Comment(id, "Comment_" + id, dbBooks.get(id - 1)))
                .toList();
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

    private static List<Comment> getDbComments() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        var books = getDbBooks(dbAuthors, dbGenres);
        return getDbComments(books);
    }
}
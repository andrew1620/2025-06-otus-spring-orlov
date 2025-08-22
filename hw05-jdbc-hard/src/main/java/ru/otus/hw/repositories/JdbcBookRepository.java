package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        var params = new MapSqlParameterSource().addValue("id", id);
        var book = jdbc.query("""
        SELECT b.id, b.title, b.author_id, a.full_name as author_full_name, bg.genre_id, g.name as genre_name
            FROM books b
            LEFT JOIN authors a ON b.author_id = a.id
            LEFT JOIN books_genres bg ON b.id = bg.book_id
            LEFT JOIN genres g ON bg.genre_id = g.id
            WHERE b.id = :id""", params, new BookResultSetExtractor());

        return Optional.ofNullable(book);
    }

    private Optional<Book> findByIdShortOrThrow(long id) {
        var params = new MapSqlParameterSource().addValue("id", id);
        var books = jdbc.query("SELECT id from books where id = :id", params, new BookShortRowMapper());
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Book with id %d doesn't exist".formatted(id));
        }
        return books.stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        findByIdShortOrThrow(id);
        var params = new MapSqlParameterSource().addValue("id", id);
        jdbc.update("DELETE FROM books WHERE id = :id", params);
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbc.query("""
                SELECT b.id, b.title, b.author_id, a.full_name AS author_full_name FROM books b
                    LEFT JOIN authors a ON b.author_id = a.id""",
                new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbc.query("SELECT book_id, genre_id FROM books_genres", new BookGenreRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        booksWithoutGenres.forEach(book -> {
            relations.forEach(bookGenre -> {
                if (book.getId() == bookGenre.bookId()) {
                    var genre = genres.stream().filter(g -> g.getId() == bookGenre.genreId()).findFirst();
                    genre.ifPresent(value -> book.getGenres().add(value));
                }
            });
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var data = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId());
        jdbc.update(
                "INSERT INTO books (title, author_id) VALUES (:title, :authorId)", data, keyHolder);
        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        findByIdShortOrThrow(book.getId());
        var data = new MapSqlParameterSource()
                .addValue("id", book.getId())
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId());
        jdbc.update("UPDATE books SET title = :title, author_id = :authorId WHERE id = :id", data);

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        var data = book.getGenres().stream()
                .map(genre -> new MapSqlParameterSource()
                        .addValue("bookId", book.getId())
                        .addValue("genreId", genre.getId())
                ).toArray(MapSqlParameterSource[]::new);

        jdbc.batchUpdate("INSERT INTO books_genres (book_id, genre_id) VALUES (:bookId, :genreId)", data);
    }

    private void removeGenresRelationsFor(Book book) {
        var data = new MapSqlParameterSource().addValue("id", book.getId());
        jdbc.update("DELETE from books_genres WHERE book_id = :id", data);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var id = rs.getLong("id");
            var title = rs.getString("title");
            var authorId = rs.getLong("author_id");
            var authorFullName = rs.getString("author_full_name");
            var author = new Author(authorId, authorFullName);

            return new Book(id, title, author, new ArrayList<Genre>());
        }
    }

    private static class BookShortRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var id = rs.getLong("id");

            return new Book(id, null, null, null);
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            while (rs.next()) {
                if (book == null) {
                    book = new Book();
                }
                var id = rs.getLong("id");
                var title = rs.getString("title");
                var authorId = rs.getLong("author_id");
                var authorFullName = rs.getString("author_full_name");
                var author = new Author(authorId, authorFullName);
                var genreId = rs.getLong("genre_id");
                var genreName = rs.getString("genre_name");
                var genre = new Genre(genreId, genreName);

                book.setId(id);
                book.setTitle(title);
                book.setAuthor(author);
                if (book.getGenres() == null) {
                    book.setGenres(new ArrayList<>());
                }
                book.getGenres().add(genre);
            }
            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }

    private static class BookGenreRowMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            var bookId = rs.getLong("book_id");
            var genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }
}

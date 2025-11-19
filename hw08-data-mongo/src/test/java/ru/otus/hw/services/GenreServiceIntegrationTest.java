package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import(GenreServiceImpl.class)
class GenreServiceIntegrationTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GenreService genreService;

    @DisplayName("Должен возвращать все жанры")
    @Test
    void findAll_ShouldReturnAllGenres() {
        List<Genre> genres = genreService.findAll();

        assertThat(genres).isNotEmpty();
        assertThat(genres).hasSizeGreaterThanOrEqualTo(1);
        genres.forEach(genre -> {
            assertThat(genre.getId()).isNotBlank();
            assertThat(genre.getName()).isNotBlank();
        });
    }

    @DisplayName("Должен возвращать пустой список когда нет жанров")
    @Test
    void findAll_WhenNoGenres_ShouldReturnEmptyList() {
        mongoTemplate.dropCollection(Genre.class);

        List<Genre> genres = genreService.findAll();

        assertThat(genres).isEmpty();
    }

    @DisplayName("Должен находить жанры по существующим IDs")
    @Test
    void findAllByIds_WithExistingIds_ShouldReturnGenres() {
        Set<String> existingIds = Set.of("1", "2");

        List<Genre> genres = genreService.findAllByIds(existingIds);

        assertThat(genres).hasSize(existingIds.size());
        genres.forEach(genre -> {
            assertThat(genre.getId()).isIn(existingIds);
            assertThat(genre.getName()).isNotBlank();
        });
    }

    @DisplayName("Должен возвращать пустой список при поиске по несуществующим IDs")
    @Test
    void findAllByIds_WithNonExistingIds_ShouldReturnEmptyList() {
        Set<String> nonExistingIds = Set.of("non-existing-1", "non-existing-2");

        List<Genre> genres = genreService.findAllByIds(nonExistingIds);

        assertThat(genres).isEmpty();
    }

    @DisplayName("Должен возвращать пустой список при поиске по пустому набору IDs")
    @Test
    void findAllByIds_WithEmptyIds_ShouldReturnEmptyList() {
        Set<String> emptyIds = Set.of();

        List<Genre> genres = genreService.findAllByIds(emptyIds);

        assertThat(genres).isEmpty();
    }

    @DisplayName("Должен возвращать только существующие жанры при смешанных IDs")
    @Test
    void findAllByIds_WithMixedIds_ShouldReturnOnlyExistingGenres() {
        String existingId = "1";
        String nonExistingId = "non-existing-id";
        Set<String> mixedIds = Set.of(existingId, nonExistingId);

        List<Genre> genres = genreService.findAllByIds(mixedIds);

        assertThat(genres).hasSize(1);
        assertThat(genres.get(0).getId()).isEqualTo(existingId);
    }
}
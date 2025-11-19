package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.hw.models.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Data-Jpa для работы с жанрами ")
@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;


    @DisplayName("должен возвращать жанры по переданным ids, если они существуют")
    @Test
    void shouldReturnGenresForExistingIds() {
        var slicedGenres = Arrays.copyOfRange(getDbGenres().toArray(), 1, 4, Genre[].class);
        var ids = Arrays.stream(slicedGenres).map(Genre::getId).collect(Collectors.toSet());

        List<Genre> result = repository.findAllByIdIn(ids);

        assertThat(result)
                .hasSize(3).usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(Arrays.stream(slicedGenres).toList());
    }

    @DisplayName("должен возвращать пустой список")
    @Test
    void shouldReturnGenresForEmptySet() {
        List<Genre> result = repository.findAllByIdIn(Set.of());

        assertThat(result)
                .hasSize(0);
    }

    @DisplayName("должен возвращать весь список жанров")
    @Test
    void shouldReturnAllGenres() {
        var genres = getDbGenres();

        List<Genre> result = repository.findAll();

        assertThat(result)
                .hasSize(6).usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(genres);
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

}
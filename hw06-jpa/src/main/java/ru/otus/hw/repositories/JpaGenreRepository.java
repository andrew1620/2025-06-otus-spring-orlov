package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JpaGenreRepository implements GenreRepository {

    private final EntityManager em;

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> genresQuery = em.createQuery("SELECT g FROM Genre g", Genre.class);
        return genresQuery.getResultList();
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g WHERE g.id IN (:ids)", Genre.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }
}

package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<Author> findAll() {
        var items = authorRepository.findAll();
        return items;
    }

    @Override
    public Optional<Author> findById(String id) {
        var author = authorRepository.findById(id);
        if (author.isEmpty()) {
            throw new EntityNotFoundException("Author with id %d doesn't exist".formatted(id));
        }
        return author;
    }
}

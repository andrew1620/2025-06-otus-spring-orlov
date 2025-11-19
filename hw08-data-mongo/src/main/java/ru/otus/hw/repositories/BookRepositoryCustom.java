package ru.otus.hw.repositories;

public interface BookRepositoryCustom {
    void deleteByIdCascade(String id);
}

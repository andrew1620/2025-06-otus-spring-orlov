package ru.otus.hw.service;

import ru.otus.hw.domain.Question;

public interface QuestionConverter {
    String questionWithAnswersToString(Question question);
}

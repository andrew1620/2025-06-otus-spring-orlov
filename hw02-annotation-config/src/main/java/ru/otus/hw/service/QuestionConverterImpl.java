package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Question;


@Service
public class QuestionConverterImpl implements QuestionConverter {
    @Override
    public String questionWithAnswersToString(Question question) {
        StringBuilder questionWithAnswers = new StringBuilder(question.text());
        var answers = question.answers();
        for (int index = 0; index < answers.size(); index++) {
            questionWithAnswers.append("\n").append(index).append(": ").append(answers.get(index).text());
        }
        return questionWithAnswers.toString();
    }
}

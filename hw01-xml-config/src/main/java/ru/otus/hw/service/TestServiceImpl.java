package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final CsvQuestionDao csvQuestionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = csvQuestionDao.findAll();
        printQuestions(questions);
    }

    private void printQuestions(List<Question> questions) {
        questions.forEach(question -> {
            ioService.printFormattedLine("%n" + question.text());
            for (int index = 0; index < question.answers().size(); index++) {
                ioService.printFormattedLine("%s: " + question.answers().get(index).text(), index);
            }
        });
    }
}

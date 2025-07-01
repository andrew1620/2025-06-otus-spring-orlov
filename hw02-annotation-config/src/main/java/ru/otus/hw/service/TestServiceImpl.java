package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConverter questionConverter;


    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var questionWithAnswers = questionConverter.questionWithAnswersToString(question);
            Integer answerIndex = printQuestionAndGetAnswerIndex(questionWithAnswers, question.answers().size() - 1);
            var answer = question.answers().get(answerIndex);
            testResult.applyAnswer(question, answer.isCorrect());
        }
        return testResult;
    }

    private Integer printQuestionAndGetAnswerIndex(String questionWithAnswers, Integer lastAnswerIndex) {
        var answerIndex = ioService.readIntForRangeWithPrompt(
                0,
                lastAnswerIndex,
                questionWithAnswers,
                "Incorrect input. Try again."
        );
        ioService.printLine("");
        return answerIndex;
    }
}

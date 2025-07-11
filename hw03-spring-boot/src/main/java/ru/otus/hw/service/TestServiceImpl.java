package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConverter questionConverter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            var questionWithAnswers = questionConverter.questionWithAnswersToString(question);
            var answerIndex = printQuestionAndGetAnswerIndex(questionWithAnswers, question.answers().size());
            var answer = question.answers().get(answerIndex - 1);
            testResult.applyAnswer(question, answer.isCorrect());
        }
        return testResult;
    }

    private Integer printQuestionAndGetAnswerIndex(String questionWithAnswers, Integer lastAnswerIndex) {
        var answerIndex = ioService.readIntForRangeWithPromptErrorMessageLocalized(
                1,
                lastAnswerIndex,
                questionWithAnswers,
                "TestService.incorrect.output"
        );
        ioService.printLine("");
        return answerIndex;
    }

}

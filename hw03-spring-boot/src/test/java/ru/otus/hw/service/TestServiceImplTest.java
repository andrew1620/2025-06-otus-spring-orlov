package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private CsvQuestionDao csvQuestionDao;

    @Mock
    private QuestionConverter questionConverter;

    @InjectMocks
    private TestServiceImpl testService;

    @Test
    public void shouldExecuteMethodWorkForExpectedStudent() {
        var student = new Student("John", "Doe");
        List<Question> expectedQuestions = getQuestions();
        given(csvQuestionDao.findAll())
                .willReturn(expectedQuestions);
        String questionWithAnswers = """
                question_1
                1: answer_1
                2: answer_2
                3: answer_3
                """;
        given(questionConverter.questionWithAnswersToString(any()))
                .willReturn(questionWithAnswers);
        given(ioService.readIntForRangeWithPromptErrorMessageLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .willReturn(3);
        var result = testService.executeTestFor(student);

        assertEquals(student, result.getStudent());
        assertEquals(1, result.getAnsweredQuestions().size());
        assertTrue(result.getRightAnswersCount() >= 0);

        verify(csvQuestionDao, times(1)).findAll();
        verify(questionConverter, times(1)).questionWithAnswersToString(any());
        verify(ioService, times(1)).readIntForRangeWithPromptErrorMessageLocalized(anyInt(), anyInt(), anyString(), anyString());
    }

    List<Question> getQuestions() {
        List<Answer> answers = List.of(
                new Answer("answer_1", false),
                new Answer("answer_2", false),
                new Answer("answer_3", true)
        );

        return List.of(new Question("question_1", answers));
    }
}

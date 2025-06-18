package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private CsvQuestionDao csvQuestionDao;

    @InjectMocks
    private TestServiceImpl testService;

    @Test
    public void shouldExecuteMethodPrintExpectedData() {
        List<Question> expectedQuestions = getQuestions();
        given(csvQuestionDao.findAll())
                .willReturn(expectedQuestions);

        testService.executeTest();

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");
        verify(ioService).printFormattedLine("%nquestion_1");
        verify(ioService).printFormattedLine("%s: answer_1", 0);
        verify(ioService).printFormattedLine("%s: answer_2", 1);
        verify(ioService).printFormattedLine("%s: answer_3", 2);
    }

    @Test
    void shouldExecuteCallDaoOnce() {
        given(csvQuestionDao.findAll()).willReturn(List.of());

        testService.executeTest();

        verify(csvQuestionDao, times(1)).findAll();
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

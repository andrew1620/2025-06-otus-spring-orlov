package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class TestServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @SpyBean
    private CsvQuestionDao csvQuestionDao;

    @SpyBean
    private QuestionConverter questionConverter;

    @Autowired
    private TestServiceImpl testService;

    @Test
    public void shouldExecuteMethodWorkForExpectedStudent() {
        var student = new Student("John", "Doe");
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
}

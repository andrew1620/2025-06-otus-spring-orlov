package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.CsvSeparatorProvider;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.utils.FileResourcesUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    @Mock
    private CsvSeparatorProvider csvSeparatorProvider;

    private final  FileResourcesUtils fileResourcesUtils = new FileResourcesUtils();

    @Test
    @DisplayName("Должен корректно читать вопросы из CSV файла")
    void shouldReadQuestionsFromCsvFile() {
        var csvQuestionDao = new CsvQuestionDao(fileNameProvider, csvSeparatorProvider, fileResourcesUtils);
        given(fileNameProvider.getTestFileName()).willReturn("questions.csv");
        given(csvSeparatorProvider.getCsvSeparator()).willReturn(';');

        List<Question> questions = csvQuestionDao.findAll();

        assertThat(questions).hasSize(1);

        Question firstQuestion = questions.get(0);
        assertThat(firstQuestion.text()).isEqualTo("Is there life on Mars?");
        assertThat(firstQuestion.answers()).hasSize(3);
        assertThat(firstQuestion.answers().get(0).isCorrect()).isTrue();
        assertThat(firstQuestion.answers().get(0).text()).isEqualTo("Science doesn't know this yet");
    }
}

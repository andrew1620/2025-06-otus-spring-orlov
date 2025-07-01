package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.CsvSeparatorProvider;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.utils.FileResourcesUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    @TempDir
    Path tempDir;

    @Mock
    private TestFileNameProvider fileNameProvider;

    @Mock
    private CsvSeparatorProvider csvSeparatorProvider;

    @Mock
    private FileResourcesUtils fileResourcesUtils;

    @Test
    @DisplayName("Должен корректно читать вопросы из CSV файла")
    void shouldReadQuestionsFromCsvFile() throws IOException {
        var csvQuestionDao = new CsvQuestionDao(fileNameProvider, csvSeparatorProvider, fileResourcesUtils);
        File csvFile = tempDir.resolve("test_questions.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("first line\n");
            writer.write("Is there life on Mars?;Science doesn't know this yet%true|Certainly. The red UFO is from Mars. And green is from Venus%false|Absolutely not%false");
        }

        given(fileNameProvider.getTestFileName()).willReturn(csvFile.getName());
        given(csvSeparatorProvider.getCsvSeparator()).willReturn(';');
        given(fileResourcesUtils.getFileFromResourceAsStream(anyString()))
                .willReturn(new java.io.FileInputStream(csvFile));

        List<Question> questions = csvQuestionDao.findAll();

        assertThat(questions).hasSize(1);

        Question firstQuestion = questions.get(0);
        assertThat(firstQuestion.text()).isEqualTo("Is there life on Mars?");
        assertThat(firstQuestion.answers()).hasSize(3);
        assertThat(firstQuestion.answers().get(0).isCorrect()).isTrue();
    }
}

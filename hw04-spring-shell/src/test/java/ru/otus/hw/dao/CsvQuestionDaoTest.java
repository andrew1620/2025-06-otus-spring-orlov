package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Test
    @DisplayName("Должен корректно читать вопросы из CSV файла")
    void shouldReadQuestionsFromCsvFile() {
        List<Question> questions = csvQuestionDao.findAll();

        assertThat(questions).hasSize(1);

        Question firstQuestion = questions.get(0);
        assertThat(firstQuestion.text()).isEqualTo("Is there life on Mars?");
        assertThat(firstQuestion.answers()).hasSize(3);
        assertThat(firstQuestion.answers().get(0).isCorrect()).isTrue();
        assertThat(firstQuestion.answers().get(0).text()).isEqualTo("Science doesn't know this yet");
    }
}

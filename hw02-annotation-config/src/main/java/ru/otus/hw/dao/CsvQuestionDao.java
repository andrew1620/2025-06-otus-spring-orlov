package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.CsvSeparatorProvider;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.utils.FileResourcesUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    private final CsvSeparatorProvider csvSeparatorProvider;

    private final FileResourcesUtils fileResourcesUtils;

    @Override
    public List<Question> findAll() {

        try (InputStream stream = fileResourcesUtils.getFileFromResourceAsStream(fileNameProvider.getTestFileName())) {

            Reader reader = new InputStreamReader(stream);

            List<QuestionDto> beans = new CsvToBeanBuilder<QuestionDto>(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(csvSeparatorProvider.getCsvSeparator())
                    .build()
                    .parse();

            return beans.stream().map(QuestionDto::toDomainObject).toList();
        } catch (IOException exception) {
            throw new QuestionReadException(exception.getMessage());
        }
    }
}

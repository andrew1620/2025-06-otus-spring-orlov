package ru.otus.hw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AppProperties implements TestConfig, TestFileNameProvider, CsvSeparatorProvider {

    private int rightAnswersCountToPass;

    private String testFileName;

    private Character csvSeparator;

    public AppProperties(
            @Value("${test.rightAnswersCountToPass}") int rightAnswersCountToPass,
            @Value("${test.fileName}") String testFileName,
            @Value("${test.csvSeparator}") Character csvSeparator
    ) {
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.testFileName = testFileName;
        this.csvSeparator = csvSeparator;
    }
}

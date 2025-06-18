package ru.otus.hw.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AppProperties implements TestFileNameProvider, CsvSeparatorProvider {
    private String testFileName;

    private Character csvSeparator;
}

package ru.otus.hw.utils;

import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.ResourceNotFoundException;

import java.io.InputStream;

@Component
public class FileResourcesUtils {
    public InputStream getFileFromResourceAsStream(String fileName) {

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new ResourceNotFoundException("File not found. Filename: " + fileName);
        } else {
            return inputStream;
        }

    }
}

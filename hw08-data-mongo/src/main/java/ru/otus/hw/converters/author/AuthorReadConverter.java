//package ru.otus.hw.converters.author;
//
//import org.bson.Document;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.data.convert.ReadingConverter;
//import ru.otus.hw.models.Author;
//
//@ReadingConverter
//public class AuthorReadConverter implements Converter<Document, Author> {
//    @Override
//    public Author convert(Document source) {
//        return new Author().setId((String) source.get("_id")).setFullName((String) source.get("full_name"));
//    }
//}

package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@ChangeLog
public class AddData1 {

    @ChangeSet(order = "001", id = "dropDb", author = "andrew", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "andrew")
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> authorCollection = db.getCollection("authors");
        var author1 = new Document().append("_id", "1").append("full_name", "Author_1");
        var author2 = new Document().append("_id", "2").append("full_name", "Author_2");
        var author3 = new Document().append("_id", "3").append("full_name", "Author_3");
        authorCollection.insertMany(List.of(author1, author2, author3));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "andrew")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> genresCollection = db.getCollection("genres");
        var authors = IntStream.rangeClosed(1, 6).mapToObj((index) -> new Document()
                .append("_id", String.valueOf(index))
                .append("name", "Genre_" + index)).toList();
        genresCollection.insertMany(authors);
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "andrew")
    public void insertBooks(MongoDatabase db) {
        MongoCollection<Document> bookCollection = db.getCollection("books");
        MongoCollection<Document> authorCollection = db.getCollection("authors");
        MongoCollection<Document> genreCollection = db.getCollection("genres");
        var authorDocs = authorCollection.find().into(new ArrayList<Document>());
        var genreDocs = genreCollection.find().into(new ArrayList<Document>());
        var books = IntStream.rangeClosed(0, authorDocs.size() - 1).mapToObj((index) -> new Document()
                .append("_id", String.valueOf(index + 1))
                .append("title", "BookTitle_" + (index + 1))
                .append("author", authorDocs.get(index))
                .append("genres", List.of(genreDocs.get(index), genreDocs.get(index + 1)))).toList();
        bookCollection.insertMany(books);
    }

    @ChangeSet(order = "005", id = "insertComments", author = "andrew")
    public void insertComments(MongoDatabase db) {
        MongoCollection<Document> commentCollection = db.getCollection("comments");
        MongoCollection<Document> bookCollection = db.getCollection("books");
        var bookDocs = bookCollection.find().into(new ArrayList<Document>());


        var comments = IntStream.rangeClosed(0, bookDocs.size() - 1).mapToObj((index) -> new Document()
                .append("_id", String.valueOf(index + 1))
                .append("title", "Comment_" + (index + 1))
                .append("book", getBookRef(bookDocs.get(index).get("_id").toString()))
                .append("_class", "ru.otus.hw.models.Comment")).toList();
        commentCollection.insertMany(comments);
    }


    private Document getBookRef(String id) {
        return new Document()
                .append("$ref", "books")
                .append("$id", id);
    }
}
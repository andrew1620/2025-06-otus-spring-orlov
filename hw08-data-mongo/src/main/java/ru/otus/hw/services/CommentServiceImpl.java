package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

//    @PostConstruct
//    private void init() {
//        List<Book> all = bookRepository.findAll();
//        System.out.println(all.size());
//    }

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

//    @Transactional
    @Override
    public Comment insert(String title, String bookId) {
        return save(null, title, bookId);
    }

//    @Transactional
    @Override
    public Comment update(String id, String title, String bookId) {
        return save(id, title, bookId);
    }

//    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

//    @Transactional
    @Override
    public List<Comment> findByBookId(String bookId) {
        var a = commentRepository.findByBookId(bookId);
        return a;
    }

    private Comment save(String id, String title, String bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));

        Comment comment = new Comment(id, title, book);
        return commentRepository.save(comment);
    }
}

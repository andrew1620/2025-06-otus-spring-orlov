package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.DeleteEntityException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RequiredArgsConstructor
@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final CommentRepository commentRepository;

    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteByIdCascade(String id) {
        var isExist = mongoTemplate.exists(query(where("_id").is(id)), Book.class);
        if (!isExist) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }

        var comments = commentRepository.findByBookId(id);
        if (!comments.isEmpty()) {
            var ids = comments.stream().map(Comment::getId).toList();
            var result = mongoTemplate.remove(query(where("_id").in(ids)), Comment.class);
            if (result.getDeletedCount() != comments.size()) {
                throw new DeleteEntityException("Comments were deleted partly. Ids: " + String.join(", ", ids));
            }
        }
        var bookDelResult = mongoTemplate.remove(query(where("_id").is(id)), Book.class);
        if (bookDelResult.getDeletedCount() == 0) {
            throw new DeleteEntityException("Book with id " + id + " wasn't deleted");
        }
    }
}

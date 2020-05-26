package ru.krestyankin.library.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krestyankin.library.models.Comment;
import ru.krestyankin.library.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @HystrixCommand(groupKey = "CommentService", commandKey = "commentSave")
    public void save(Comment comment){
        commentRepository.save(comment);
    }

    @HystrixCommand(groupKey = "CommentService", commandKey = "commentDeleteById")
    public void deleteById(String commentId){
        commentRepository.deleteById(commentId);
    }

    @HystrixCommand(groupKey = "CommentService", commandKey = "getCommentsByBook"
            , fallbackMethod = "emptyCommentsList")
    public List<Comment> getCommentsByBook(String bookId) {
        return commentRepository.getCommentsByBook(bookId);
    }

    public List<Comment> emptyCommentsList(String bookId) {
        return new ArrayList<>();
    }
}

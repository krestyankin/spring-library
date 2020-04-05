package ru.krestyankin.library.service;

public interface CommentService {
    void addComment(String bookId);
    void deleteComment(String bookId);
}

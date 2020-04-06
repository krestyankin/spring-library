package ru.krestyankin.library.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;
@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.krestyankin.library.config", "ru.krestyankin.library.repositories", "ru.krestyankin.library.event"})
@DisplayName("CommentRepository должен ")
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    BookRepository bookRepository;

    @DisplayName(" добавлять и удалять комментарии")
    @Test
    void shouldCorrectAddComment(){
        Book book = bookRepository.findAll().get(1);
        Comment comment = new Comment();
        comment.setBook(book);
        comment.setText("Comment text");
        commentRepository.save(comment);
        assertThat(commentRepository.getCommentsByBook(book.getId())).isNotNull().hasSize(1).allMatch(c -> c.getText().equals(comment.getText()));
        long initialCommentsCount = commentRepository.count();
        commentRepository.delete(comment);
        assertThat(commentRepository.getCommentsByBook(book.getId())).hasSize(0);
        assertThat(commentRepository.count()).isEqualTo(initialCommentsCount-1);
    }

    @DisplayName(" удалять все комментарии книги")
    @Test
    void shouldCorrectDeleteAllBookComments() {
        Book book = bookRepository.findAll().get(0);
        commentRepository.deleteAllByBook(book.getId());
        assertThat(commentRepository.getCommentsByBook(book.getId())).hasSize(0);
    }

    @DisplayName(" должен корректно получать комментарии книги")
    @Test
    void shouldCorrectFetchBookComment() {
        Book book = bookRepository.findAll().get(2);
        assertThat(commentRepository.getCommentsByBook(book.getId())).isNotNull().hasSize(3).
                allMatch(comment -> comment.getId()!=null && comment.getText().startsWith("Комментарий "));
    }

}
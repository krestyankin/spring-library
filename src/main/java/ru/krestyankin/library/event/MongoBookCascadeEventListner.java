package ru.krestyankin.library.event;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.repositories.CommentRepository;

@Component
public class MongoBookCascadeEventListner extends AbstractMongoEventListener<Book> {
    private final CommentRepository commentRepository;

    public MongoBookCascadeEventListner(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<Book> event) {
        super.onAfterDelete(event);
        commentRepository.deleteAllByBook(event.getSource().get("_id").toString());
    }
}

package ru.krestyankin.library.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.krestyankin.library.models.*;
import ru.krestyankin.library.repositories.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {
    private final List<Author> authors;
    private final List<Genre> genres;

    public InitMongoDBDataChangeLog() throws ParseException {
        //this.passwordEncoder = new BCryptPasswordEncoder(10);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        authors = List.of(new Author("Joshua J. Bloch", formatter.parse("28.08.1961")),
                new Author("Агата Кристи", formatter.parse("15.09.1890")),
                new Author("Илья Ильф", formatter.parse("03.10.1897")),
                new Author("Евгений Петров", formatter.parse("03.10.1897"))
        );
        genres = List.of(new Genre("Программирование"), new Genre("Java"), new Genre("Детектив"), new Genre("Сатира"));
    }

    @ChangeSet(order = "000", id = "dropDB", author = "dkrestyankin", runAlways = true)
    public void dropDB(MongoDatabase database){
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "dkrestyankin", runAlways = true)
    public void initAuthors(MongoTemplate template) throws ParseException {
        for (Author author : authors) {
            template.save(author);
        }
    }

    @ChangeSet(order = "002", id = "initGenres", author = "dkrestyankin", runAlways = true)
    public void initGenres(MongoTemplate template){
        for (Genre genre: genres) {
            template.save(genre);
        }
    }

    @ChangeSet(order = "004", id = "initBooks", author = "dkrestyankin", runAlways = true)
    public void initBooks(MongoTemplate template, NamedParameterJdbcTemplate jdbc){
        Book book = new Book();
        book.setTitle("Effective Java: Programming Language Guide");
        book.setAuthors(List.of(authors.get(0)));
        book.setGenres(List.of(genres.get(0), genres.get(1)));
        book=template.save(book);
        for(int i=1;i<=5;i++)
            template.save(new Comment("Comment "+i, book));
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(Book.class, book.getId());
        jdbc.update("INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES\n" +
                "(1, 1, :bookId, NULL, (select id from acl_sid where sid='ROLE_ADMIN'), 0)", Collections.singletonMap("bookId", book.getId()));
        // неудаляемая книга
        jdbc.update("INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES\n" +
                "(1, 1, (select id from acl_sid where sid='ROLE_ADMIN'), 1, 1, 1, 1)", Collections.EMPTY_MAP);
        jdbc.update("INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES\n" +
                "(1, 2, (select id from acl_sid where sid='ROLE_READER'), 1, 1, 1, 1)", Collections.EMPTY_MAP);



        book = new Book();
        book.setTitle("Убийство в «Восточном экспрессе»");
        book.setAuthors(List.of(authors.get(1)));
        book.setGenres(List.of(genres.get(2)));
        book=template.save(book);
        jdbc.update("INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES\n" +
                "(2, 1, :bookId, NULL, (select id from acl_sid where sid='ROLE_ADMIN'), 0)", Collections.singletonMap("bookId", book.getId()));
        jdbc.update("INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES\n" +
                "(2, 1, (select id from acl_sid where sid='ROLE_ADMIN'), 16, 1, 1, 1)", Collections.EMPTY_MAP);
        // книга для взрослых
        jdbc.update("INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES\n" +
                "(2, 2, (select id from acl_sid where sid='ROLE_ADULT'), 1, 1, 1, 1)", Collections.EMPTY_MAP);
        book = new Book();
        book.setTitle("Двенадцать стульев");
        book.setAuthors(List.of(authors.get(2), authors.get(3)));
        book.setGenres(List.of(genres.get(3)));
        book=template.save(book);
        for(int i=1;i<=3;i++)
            template.save(new Comment("Комментарий "+i, book));
        jdbc.update("INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES\n" +
                "(3, 1, :bookId, NULL, (select id from acl_sid where sid='ROLE_ADMIN'), 0)", Collections.singletonMap("bookId", book.getId()));
        jdbc.update("INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES\n" +
                "(3, 1, (select id from acl_sid where sid='ROLE_ADMIN'), 16, 1, 1, 1)", Collections.EMPTY_MAP);
        jdbc.update("INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES\n" +
                "(3, 2, (select id from acl_sid where sid='ROLE_READER'), 1, 1, 1, 1)", Collections.EMPTY_MAP);
    }

    @ChangeSet(order = "003", id = "initUsers", author = "dkrestyankin", runAlways = true)
    public void initUsers(MongoTemplate template, UserRepository userRepository, PasswordEncoder passwordEncoder){
        userRepository.save(new User("admin", passwordEncoder.encode("password"),"ROLE_ADMIN,ROLE_READER,ROLE_ADULT"));
        userRepository.save(new User("schoolboy", passwordEncoder.encode("password"), "ROLE_READER"));
        userRepository.save(new User("student", passwordEncoder.encode("password"), "ROLE_READER,ROLE_ADULT"));
    }

}

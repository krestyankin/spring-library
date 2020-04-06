package ru.krestyankin.library.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.krestyankin.library.models.Author;
import ru.krestyankin.library.models.Book;
import ru.krestyankin.library.models.Comment;
import ru.krestyankin.library.models.Genre;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {
    private final List<Author> authors;
    private final List<Genre> genres;

    public InitMongoDBDataChangeLog() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        authors = List.of(new Author("Joshua J. Bloch", formatter.parse("28.08.1961")),
                new Author("Агата Кристи", formatter.parse("15.09.1890")),
                new Author("Илья Ильф", formatter.parse("03.10.1897")),
                new Author("Евгений Петров", formatter.parse("03.10.1897"))
        );
        genres = List.of(new Genre("'Программирование'"), new Genre("'Java'"), new Genre("'Детектив'"), new Genre("'Сатира'"));
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

    @ChangeSet(order = "003", id = "initBooks", author = "dkrestyankin", runAlways = true)
    public void initBooks(MongoTemplate template){
        Book book = new Book();
        book.setTitle("Effective Java: Programming Language Guide");
        book.setAuthors(List.of(authors.get(0)));
        book.setGenres(List.of(genres.get(0), genres.get(1)));
        book=template.save(book);
        for(int i=1;i<=5;i++)
            template.save(new Comment("Comment "+i, book));

        book = new Book();
        book.setTitle("Убийство в «Восточном экспрессе»");
        book.setAuthors(List.of(authors.get(1)));
        book.setGenres(List.of(genres.get(2)));
        template.save(book);
        book = new Book();
        book.setTitle("Двенадцать стульев");
        book.setAuthors(List.of(authors.get(2), authors.get(3)));
        book.setGenres(List.of(genres.get(3)));
        template.save(book);
        for(int i=1;i<=3;i++)
            template.save(new Comment("Комментарий "+i, book));
    }

}

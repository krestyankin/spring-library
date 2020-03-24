DROP TABLE IF EXISTS BOOKS;
CREATE TABLE BOOKS(ID          bigserial,
                   TITLE       VARCHAR(255));

DROP TABLE IF EXISTS AUTHORS;
CREATE TABLE AUTHORS(ID        bigserial,
                     FULLNAME  VARCHAR(255),
                     DOB       DATE
                     );

DROP TABLE IF EXISTS BOOKS_AUTHORS;
CREATE TABLE BOOKS_AUTHORS(BOOK_ID     BIGINT,
                           AUTHOR_ID   BIGINT);

DROP TABLE IF EXISTS GENRES;
CREATE TABLE GENRES(ID         bigserial,
                    NAME       VARCHAR(255));

DROP TABLE IF EXISTS BOOKS_GENRES;
CREATE TABLE BOOKS_GENRES(BOOK_ID     BIGINT,
                          GENRE_ID    BIGINT);


DROP TABLE IF EXISTS COMMENTS;
CREATE TABLE COMMENTS(ID          bigserial,
                      BOOK_ID     bigint references books(id) on delete cascade,
                      TEXT        VARCHAR(255));
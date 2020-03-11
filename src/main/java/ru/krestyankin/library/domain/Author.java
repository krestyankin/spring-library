package ru.krestyankin.library.domain;

import lombok.Getter;

import java.util.Date;

@Getter
public class Author {
    private long id;
    private String fullname;
    private Date dateOfBirth;

    public Author(long id, String fullname, Date dateOfBirth) {
        this.id = id;
        this.fullname = fullname;
        this.dateOfBirth = dateOfBirth;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}

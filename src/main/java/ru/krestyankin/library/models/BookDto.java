package ru.krestyankin.library.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookDto {
    private String id;
    private String title;
    private List<String> authors;
    private List<String> genres;
}

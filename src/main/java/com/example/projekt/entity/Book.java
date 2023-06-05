package com.example.projekt.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
public class Book {

    @Id
    private String id;
    private String title;
    private String author;
    private LocalDate issuedAt;
    private String ISBN;

    public Book() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getIssuedAt() {
        return issuedAt;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setId(String id) {
        this.id = id;
    }
}
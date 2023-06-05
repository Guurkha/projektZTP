package com.example.projekt.controllers;

import com.example.projekt.entity.Book;
import com.example.projekt.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BooksService bookService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Book> getAllBooks() {
        return bookService.getBooks();
    }

    @GetMapping(value = "/year", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Book> getAllBooksByYear(@RequestParam(name = "year", required = true) Integer year) {
        return bookService.getBooks(year);
    }



    @GetMapping(value = "/author", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Book> getAllBooksByAuthor(@RequestParam(name = "author", required = true) String author) {
        return bookService.getBooks(author);
    }



    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Book> getSingleBook(@PathVariable("id") String id) {
        return bookService.getBook(id);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Book> createBook(@RequestBody Mono<Book> book) {
        return bookService.saveBook(book);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Book> updateBook(@RequestBody Mono<Book> book, @PathVariable("id") String id) {
        return bookService.updateBook(book, id);
    }

    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> deleteBook(@PathVariable(value = "id") String id) {
        return bookService.deleteBook(id);
    }

}
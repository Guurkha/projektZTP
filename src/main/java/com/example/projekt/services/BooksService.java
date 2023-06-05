package com.example.projekt.services;

import com.example.projekt.entity.Book;
import com.example.projekt.entity.BookDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BooksService {

    @Autowired
    private BookDataBase booksDataBase;
    @Autowired
    private ReactiveMongoTemplate mongoDataBaseTemp;

    public Flux<Book> getBooks() {
        return booksDataBase.findAll();
    }

    public Flux<Book> getBooks(String author) {

        if (author != null) {
            Query query = Query.query(Criteria.where("author").is(author));
            return mongoDataBaseTemp.find(query, Book.class);
        }
        return booksDataBase.findAll();
    }
    public Flux<Book> getBooks(Integer year) {
        if (year != null) {
            ProjectionOperation projectOperation = Aggregation.project("title", "author", "issuedAt", "ISBN")
                    .andExpression("year(issuedAt)").as("year");

            MatchOperation matchOperation = Aggregation.match(Criteria.where("year").is(year));

            Aggregation aggregation = Aggregation.newAggregation(projectOperation, matchOperation);

            return mongoDataBaseTemp.aggregate(aggregation, "book", Book.class);
        }

        return booksDataBase.findAll();
    }

    public Mono<Book> getBook(String id) {
        return booksDataBase.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")));
    }

    public Mono<Book> saveBook(Mono<Book> book) {
        return book.flatMap(booksDataBase::insert);
    }

    public Mono<Book> updateBook(Mono<Book> book, String id) {
        return booksDataBase.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"))).flatMap(b -> book)
                .doOnNext(updatedBook -> updatedBook.setId(id))
                .flatMap(booksDataBase::save);
    }

    public Mono<Void> deleteBook(String id) {
        return booksDataBase.deleteById(id);
    }
}
package com.example.projekt.services;

import com.example.projekt.entity.Book;
import com.example.projekt.entity.BookDataBase;
import com.example.projekt.entity.Cart;
import com.example.projekt.entity.CartDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class CartService {

    @Autowired
    private CartDataBase cartRepository;
    @Autowired
    private BookDataBase bookRepository;
    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public Flux<Book> getCart() {
        return mongoTemplate.findOne(new Query().limit(1), Cart.class)
                .flatMapMany(cart -> Flux.fromIterable(cart.getBooks()));
    }

    public Flux<Book> addBookToCart(String bookId) {
        Mono<Cart> cartMono = mongoTemplate.findOne(new Query().limit(1), Cart.class);
        Mono<Book> bookMono = bookRepository.findById(bookId);

        return cartMono.zipWith(bookMono)
                .flatMap(tuple -> {
                    Cart cart = tuple.getT1();
                    Book book = tuple.getT2();

                    List<Book> books = cart.getBooks();
                    books.add(book);
                    cart.setBooks(books);
                    return cartRepository.save(cart);
                })
                .flatMapMany(savedCart -> Flux.fromIterable(savedCart.getBooks()));
    }

    public Flux<Book> deleteBookFromCart(String bookId) {
        Mono<Cart> cartMono = mongoTemplate.findOne(new Query().limit(1), Cart.class);
        return cartMono
                .flatMap(cart -> {
                    List<Book> books = cart.getBooks();
                    Iterator<Book> iterator = books.iterator();
                    while (iterator.hasNext()) {
                        Book next = iterator.next();
                        if (Objects.equals(next.getId(), bookId)) {
                            iterator.remove();
                            break;
                        }
                    }
                    cart.setBooks(books);
                    return cartRepository.save(cart);
                })
                .flatMapMany(savedCart -> Flux.fromIterable(savedCart.getBooks()));
    }
}
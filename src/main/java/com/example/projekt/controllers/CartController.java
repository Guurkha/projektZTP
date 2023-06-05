package com.example.projekt.controllers;

import com.example.projekt.entity.Book;
import com.example.projekt.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/cart")
public class CartController {


    @Autowired
    private CartService service;

    @GetMapping
    public Flux<Book> getCart() {
        return service.getCart();
    }

    @PostMapping
    public Flux<Book> addBookToCart(@RequestParam(value = "id") String id) {
        return service.addBookToCart(id);
    }

    @DeleteMapping
    public Flux<Book> deleteBookFromCart(@RequestParam(value = "id") String id) {
        return service.deleteBookFromCart(id);
    }

}
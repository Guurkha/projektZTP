package com.example.projekt.entity;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CartDataBase extends ReactiveMongoRepository<Cart, String> {
}
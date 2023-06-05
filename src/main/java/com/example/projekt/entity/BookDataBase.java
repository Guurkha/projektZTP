package com.example.projekt.entity;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDataBase extends ReactiveMongoRepository<Book, String> {
}
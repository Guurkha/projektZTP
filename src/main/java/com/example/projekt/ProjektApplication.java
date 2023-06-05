package com.example.projekt;

import com.example.projekt.entity.CartDataBase;
import com.example.projekt.entity.Cart;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ProjektApplication {
    @Autowired
    private CartDataBase cartRepository;


    @EventListener(value = ApplicationReadyEvent.class)
    public void createCart() {
        cartRepository.count()
                .flatMap(countCart -> {
                    if (countCart == 0L) {
                        Cart cart = new Cart();
                        return cartRepository.insert(cart);
                    }
                    return Mono.empty();
                })
                .subscribe();
    }

    public static void main(String[] args) {
        SpringApplication.run(ProjektApplication.class, args);
    }

}

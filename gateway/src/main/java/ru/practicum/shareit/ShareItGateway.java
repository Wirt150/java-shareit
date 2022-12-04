package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ShareItGateway {
    public static void main(String[] args) {
        SpringApplication.run(ShareItGateway.class, args);
    }
}

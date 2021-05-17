package com.azizi.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.azizi")
public class PostMsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostMsApplication.class, args);
    }

}

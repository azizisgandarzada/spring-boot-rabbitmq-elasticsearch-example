package com.azizi.post.indexer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.azizi")
@EnableAsync
public class PostIndexerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostIndexerApplication.class, args);
    }

}

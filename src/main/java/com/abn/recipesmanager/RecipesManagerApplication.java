package com.abn.recipesmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.abn.recipesmanager.adapter")
public class RecipesManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipesManagerApplication.class, args);
    }

}

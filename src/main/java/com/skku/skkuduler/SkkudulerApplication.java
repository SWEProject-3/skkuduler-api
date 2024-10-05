package com.skku.skkuduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SkkudulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkkudulerApplication.class, args);
    }

}

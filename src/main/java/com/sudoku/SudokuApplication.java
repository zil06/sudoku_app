package com.sudoku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SudokuApplication {
    public static void main(String[] args) {

        // パスワードHASH値の初期設定用(defaultパスワード＝password123)
        System.out.println("=== HASH ===");
        System.out.println(new BCryptPasswordEncoder().encode("password123"));
        System.out.println("============");

        SpringApplication.run(SudokuApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

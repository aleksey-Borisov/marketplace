package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/ping")
    public String ping() {
        return jdbcTemplate.queryForObject(
                "SELECT message FROM example ORDER BY id DESC LIMIT 1",
                String.class
        );
    }
}
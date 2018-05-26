package com.ex.movierater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MovieraterApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(MovieraterApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MovieraterApplication.class);
    }
}

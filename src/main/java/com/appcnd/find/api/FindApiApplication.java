package com.appcnd.find.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.appcnd.find.api.dao")
public class FindApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindApiApplication.class, args);
    }
}

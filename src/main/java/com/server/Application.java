package com.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by classic1999 on 16/9/9.
 */
@SpringBootApplication(scanBasePackages = "com.server")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}

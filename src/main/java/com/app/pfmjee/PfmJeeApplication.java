package com.app.pfmjee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.app.pfmjee")
public class PfmJeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PfmJeeApplication.class, args);
    }

}

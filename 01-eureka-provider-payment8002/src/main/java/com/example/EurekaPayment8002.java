package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author luminion
 * @version 1.0
 */
@SpringBootApplication
@EnableEurekaClient
public class EurekaPayment8002 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaPayment8002.class, args);
    }
}

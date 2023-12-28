package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author booty
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SeataPayment9005 {
    public static void main(String[] args) {
        SpringApplication.run(SeataPayment9005.class, args);
    }
}

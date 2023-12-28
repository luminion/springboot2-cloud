package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
/**
 * @author booty
 * @version 1.0
 */
@SpringBootApplication
@EnableEurekaClient
public class OpenFeignEurekaPayment8001 {
    public static void main(String[] args) {
        SpringApplication.run(OpenFeignEurekaPayment8001.class, args);
    }
}

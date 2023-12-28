package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 注解@EnableCircuitBreaker：开启hystrix服务熔断降级
 * 注解@EnableDiscoveryClient 允许通过服务发现来获取eureka服务中的服务信息
 * 注解@EnableFeignClients    允OpenFeign远程服务调用
 *
 * @author booty
 * @version 1.0
 * @date 2021/3/18 9:45
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class HystrixOpenFeignEurekaOrder80 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixOpenFeignEurekaOrder80.class, args);
    }
}
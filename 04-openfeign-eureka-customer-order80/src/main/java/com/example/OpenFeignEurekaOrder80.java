package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 注解@EnableFeignClients    允OpenFeign远程服务调用
 * 注解@EnableDiscoveryClient 允许通过服务发现来获取eureka服务中的服务信息
 * 注解@EnableEurekaClient    允许向eureka注册中心注册(可以不添加该注解)
 *
 * @author booty
 * @version 1.0
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class OpenFeignEurekaOrder80 {
    public static void main(String[] args) {
        SpringApplication.run(OpenFeignEurekaOrder80.class, args);
    }
}
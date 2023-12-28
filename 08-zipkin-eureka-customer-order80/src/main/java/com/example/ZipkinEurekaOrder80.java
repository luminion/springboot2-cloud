package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 注解@EnableDiscoveryClient 允许通过服务发现来获取eureka服务中的服务信息
 * 注解@EnableEurekaClient    允许向eureka注册中心注册(可以不添加该注解)
 *
 * @author booty
 * @version 1.0
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class ZipkinEurekaOrder80 {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinEurekaOrder80.class, args);
    }
}
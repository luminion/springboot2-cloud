package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 注解@EnableDiscoveryClient 允许通过服务发现来获取服务中的服务信息
 *
 * @author booty
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SentinelOpenFeignNacosOrder84 {
    public static void main(String[] args) {
        SpringApplication.run(SentinelOpenFeignNacosOrder84.class, args);
    }
}
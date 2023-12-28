package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 注解@EnableDiscoveryClient 允许通过服务发现来获取服务中的服务信息
 *
 * @author booty
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosOrder83 {
    public static void main(String[] args) {
        SpringApplication.run(NacosOrder83.class, args);
    }
}
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * 注解@EnableDiscoveryClient 允许通过服务发现来获取服务中的服务信息
 *
 * 若要启动多个该服务，通过idea启动的VM-options配置-Dserver.port=xxx添加启动项启动多个
 *
 * @author booty
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SentinelOpenFeignNacosPayment9003 {
    public static void main(String[] args) {
        SpringApplication.run(SentinelOpenFeignNacosPayment9003.class, args);
    }
}

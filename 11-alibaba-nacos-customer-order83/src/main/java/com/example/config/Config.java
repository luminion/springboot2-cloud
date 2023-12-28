package com.example.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 配置负载均衡
 *
 * @author booty
 * @version 1.0
 */
@Configuration
public class Config {
    /**
     * RestTemplate结合Ribbon做负载均衡一定要加@LoadBalanced注解
     * @return RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}

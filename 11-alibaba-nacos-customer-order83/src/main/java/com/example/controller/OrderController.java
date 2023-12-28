package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author booty
 * @version 1.0
 */
@RestController
@RequestMapping("order")
public class OrderController {
    /**
     * 因为在yml中配置了service-url.nacos-user-service，
     * 这里不需要再定义要访问微服务名常量，而是通过spring注入
     */
    @Value("${service-url.nacos-user-service}")
    private String url;

    /**
     * 使用restTemplate使用时需要在配置类中事先注入
     * RestTemplate结合Ribbon做负载均衡一定要加@LoadBalanced注解
     *
     */
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/getPort")
    public String paymentInfo(){
        return restTemplate.getForObject(url+"/payment/getPort/",String.class);
    }


}

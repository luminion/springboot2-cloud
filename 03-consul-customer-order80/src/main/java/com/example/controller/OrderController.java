package com.example.controller;


import entity.CommonResult;
import entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


/**
 * @author booty
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    /**
     * 使用restTemplate通过连接调用payment
     * restTemplate使用时需要在配置类中事先注入
     */
    @Resource
    RestTemplate restTemplate;


    /**
     * 通过在zooKeeper上注册过的微服务名称调用payment
     * 注：zooKeeper上的名字不同于Eureka，不是全大写，若对应不上会报错
     */
    private static final String PAYMENT_NAME = "http://provider-payment";

    @GetMapping("/getPaymentPort")
    public String getPaymentPort() {
        return restTemplate.getForObject(PAYMENT_NAME + "/payment/getPort", String.class);
    }


}
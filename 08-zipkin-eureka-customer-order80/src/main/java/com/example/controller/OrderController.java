package com.example.controller;


import com.example.loadBanlance.MyLoadBalancer;
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
import java.net.URI;
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
     * 使用网址调用payment服务获取结果的地址
     */
    private static final String PAYMENT_URL = "http://localhost:8001";


    /**
     * 原始写法
     * 通过javaAPI调用调用payment，
     */

    @GetMapping("get/{id}1")
    public String get(@PathVariable("id") long id) {
        String s = "order默认字符串";
        try {
            URL url = new URL(PAYMENT_URL + "/payment/get/" + id);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            log.info(bufferedReader.readLine());
            s = bufferedReader.readLine();
        } catch (Exception e) {
            log.error("访问抛出异常");
        }
        return s;
    }


    /**
     * 使用restTemplate通过连接调用payment
     * restTemplate使用时需要在配置类中事先注入
     * <p>
     * restTemplated的方法：
     * get/postForObject和get/postForEntity的区别：
     * forObject:将响应体中的数据转化成对象，基本可以离洁为json
     * forEntity：返回对象为ResponseEntity，包含了响应中的一些信息，如：请求头，响应状态码，响应体等
     */
    @Resource
    RestTemplate restTemplate;


    @GetMapping("get1/{id}1")
    public CommonResult<?> get1(@PathVariable("id") long id) {
        //此处发送的是get请求，payment接口接收参数格式需与此处拼接的一致，该种写法为使用了restful风格的写法（同本方法接收参数的方式）
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
    }

    @PostMapping("/add1")
    public CommonResult<?> add1(Payment payment) {
        //使用post请求
        return restTemplate.postForObject(PAYMENT_URL + "/payment/add", payment, CommonResult.class);
    }


    /**
     * 通过在eureka上注册过的微服务名称调用payment
     * 需要在pom文件中添加客户端依赖，
     * 并开启服务注册eureka注册中心（新建项目添加服务端依赖）
     * <p>
     * 若开启了多个payment服务，
     * 需要在config类中restTemplate的创建方法上添加@LoadBalanced注解
     * eureka默认会采用轮询的方法选择调用的服务
     * 如需自己配置，在config类中将实现了iRule接口的类放到配置到容器中即可
     */
    private static final String PAYMENT_NAME = "http://PROVIDER-PAYMENT";

    @GetMapping("get2/{id}")
    public CommonResult<?> get2(@PathVariable("id") long id) {
        //此处发送的是get请求，payment接口接收参数格式需与此处拼接的一致，该种写法为使用了restful风格的写法（同本方法接收参数的方式）
        return restTemplate.getForObject(PAYMENT_NAME + "/payment/get/" + id, CommonResult.class);
    }

    @PostMapping("/add2")
    public CommonResult<?> add2(Payment payment) {
        //使用post请求
        return restTemplate.postForObject(PAYMENT_NAME + "/payment/add", payment, CommonResult.class);
    }


    /**
     * 通过服务发现来获取eureka服务中的服务信息
     * 需要在主启动类添加@EnableDiscoveryClient注解
     */
    @Resource
    DiscoveryClient discoveryClient;

    private static final String PAYMENT_REG_NAME = "PROVIDER-PAYMENT";

    @GetMapping("/getInfo")
    public Object getInfo() {

        //获取所有服务的信息
        List<String> serviceList = discoveryClient.getServices();
        log.info("---------------------------");
        serviceList.forEach(log::info);

        //获取在eureka注册的指定名称的客户端实例
        List<ServiceInstance> instanceList = discoveryClient.getInstances(PAYMENT_REG_NAME);
        instanceList.forEach(e -> log.info(e.getInstanceId() + "\t" + e.getHost() + e.getPort() + e.getUri()));
        log.info("---------------------------");
        return this.discoveryClient;
    }


    /**
     * 使用自定义负载均衡的规则选择服务器访问
     * 启用时单独启动指定方法，并需要将config配置类中的@LoadBalanced注解去掉，以确实使用的是自己定义的规则
     * 如果不注释掉会报错，因为这里不能直接访问地址，需要把地址改成调用的uri在eureka上注册的application.name
     */
    @Resource
    MyLoadBalancer loadBalancer;

    @GetMapping("getPaymentPort")
    public String getPaymentPort() {
        //获取注册的指定名称的客户端实例
        List<ServiceInstance> instances = discoveryClient.getInstances(PAYMENT_REG_NAME);
        if (instances == null || instances.size() <= 0) {
            return "服务器无实例，获取失败";
        }
        //调用自定义loadBalancer的方法获取本次应该调用的服务器
        ServiceInstance serviceInstance = loadBalancer.getWhichInstance(instances);
        //获取服务器的访问链接
        URI uri = serviceInstance.getUri();
        int port = serviceInstance.getPort();
        //发送请求
        return restTemplate.getForObject(uri + ":" + port + "/payment/getPort", String.class);
    }

    /**
     * 测试zipkin调用链监控
     * 需要下载zipkin jar包并开启服务
     * 并在配置文件中指定zipkin的服务器地址并指定监控规则
     * 前往http://localhost:9411/查看调用链信息
     *
     * 下载链接：
     * https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/
     *
     * @return zipkin信息
     */
    @GetMapping("zipkin")
    public String zipkin() {
        //发送请求
        return restTemplate.getForObject(PAYMENT_NAME + "/payment/zipkin", String.class);
    }


}
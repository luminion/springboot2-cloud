package com.example.controller;


import com.example.loadBanlance.MyLoadBalancer;
import com.example.service.PaymentFeignService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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
 * @date 2021/3/18 9:43
 */
@RestController
@Slf4j
@RequestMapping("/order")
@DefaultProperties(defaultFallback = "defaultGlobalFallBack")
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

    public static final String PAYMENT_REG_NAME = "PROVIDER-PAYMENT";

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
        //获取服务器的访问链接和端口
        URI uri = serviceInstance.getUri();
        //发送请求
        return restTemplate.getForObject(uri + "/payment/getPort", String.class);
    }


    /**
     * 添加openFeign服务进行调用
     * 需要在主启动类添加@EnableFeignClients
     * 并在接口PaymentFeignService上添加对应服务的注册名称
     * 然后接口内的方法向指定服务器发送请求
     */

    @Resource
    private PaymentFeignService paymentFeignService;

    @GetMapping(value = "getPaymentPortByOpenFeign")
    public String getPaymentById() {
        return paymentFeignService.getPort();
    }

    /**
     * 测试远程服务调用的超时（openfeign调用默认超过1秒抛出异常）
     * 当远程服务休眠时间大于在配置文件中指定的等待响应时间时，将会抛出超时异常
     *
     * OpenFeign相关的配置文件内容见配置文件的ribbon项
     *
     * 指定日志打印OpenFeign接口的详细信息：
     * 1.在配置文件中指定OpenFeign接口所在的包，
     * 2.在配置类中指定容器中的Logger.Level类，返回Logger.Level.FULL；
     *
     *
     * @param second 指定远程服务的休眠时间
     * @return 休眠经过的时间或异常
     */

    @GetMapping("/timeOut/{second}")
    String timeOut(@PathVariable("second") int second) {
        return paymentFeignService.timeOut(second);
    }




    /*
    服务降级：
    调用的服务器（或方法）忙，为了不让客户端一直等待，使用备用方案处理或返回一个友好提示
    触发情况：程序运行异常，超时，服务熔断触发降级，线程池、信号量打满

    服务熔断：
    达到最大访问量时直接拒绝访问，然后或触发服务降级
    是应对服务器雪崩效应的一种微服务链路保护机制。当某个微服务出错不可用或响应时间太长时，使用备用方案，快速返回响应信息，
    进而熔断该微服务的调用，当检测到正常时再恢复
    熔断后调用该服务在一段时间内不论是否能正常执行调用时都会自动降级
    某种情况了来说，降级是概念，熔断是实现，熔断也是服务降级的其中一种实现

    hystrix会监控服务的调用情况，当达到阈值，缺省为5秒内20次调用失败，会启动熔断机制，熔断的注解同降级，为@HystrixCommand

    熔断具体使用见被调用者payment项目
     */

    /**
     *
     * 当指定服务调用超时时自动降级
     * 自动降级仅对添加了@HystrixCommand注解的方法有效
     * 并在注解内指定 @HystrixProperty注解配置，指定超时时间对应的值和类
     * 此处指定超时3秒后调用备用方法
     *
     * 此种方法比较麻烦，每个方法都需要单独指定降级方案（一般调用者不采用，仅被调用者用来做处理）
     *
     * @param second 超时时间
     * @return 休眠经过的时间或降级处理后的文字
     *
     */
    @HystrixCommand(fallbackMethod = "timeOutSingleFallBack",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
    })
    @GetMapping("/timeOutSingle/{second}")
    String timeOutSingle(@PathVariable("second") int second) {
        return paymentFeignService.timeOut(second);

    }

    /**
     * timeOutSingle方法调用超过3秒时被调用的降级方案
     *
     * @param second 超时时间
     * @return 休眠经过的时间或降级处理后的文字
     */
    String timeOutSingleFallBack(@PathVariable("second") int second){
        return "order端=====timeOutSingle方法的超时FallBack结果,timeOutSingle调用服务的时间超过3秒，调用时指定休眠的值为："+second;
    }


    /**
     * 当指定服务调用超时或异常时自动降级（默认1秒）
     * 使用 @HystrixCommand注解，使用其调用备用的默认方法处理
     * 注：需要在本类上添加@DefaultProperties注解
     * 并在注解中使用defaultFallback = "方法名" 指定默认备用方法的名称
     *
     * 此种方法相对于上一种，不需要单独为每个方法调用异常时的降级方案（一般调用者不采用，仅被调用者用来做处理）
     * 调用者的最佳方案为：在调用的远程服务接口内做处理，统一规定降级方案
     *
     * @param second 超时时间
     * @return 休眠经过的时间或降级处理后的文字
     *
     */
    @HystrixCommand
    @GetMapping("/timeOutGlobal/{second}")
    String timeOutGlobal(@PathVariable("second") int second) {
        return paymentFeignService.timeOut(second);

    }

    /**
     * 默认的调用异常或超时的降级方案
     * 方法上添加了@HystrixCommand注解的，调用异常时会使用该方法
     * 注：需要在本类上添加@DefaultProperties注解
     * 并在注解中使用defaultFallback = "方法名" 指定默认备用方法的名称
     *
     */
    String defaultGlobalFallBack(){
        return "order端=====默认的超时FallBack结果";
    }

}
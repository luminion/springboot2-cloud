package com.example.controller;


import com.example.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import entity.CommonResult;
import entity.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author booty
 * @since 2021-03-18
 */
@RestController
@RequestMapping("/payment")
@DefaultProperties(defaultFallback = "defaultGlobalFallBack")
public class PaymentController {
    @Resource
    PaymentService paymentService;

    @GetMapping("/get/{id}")
    public CommonResult<?> get(@PathVariable("id") long id) {
        Payment payment = paymentService.getById(id);
        if (null != payment) {
            return new CommonResult<>(200, "操作成功", payment);
        }
        return CommonResult.fail();
    }

    @PostMapping("/add")
    public CommonResult<?> add(@RequestBody Payment payment) {
        boolean save = paymentService.save(payment);
        return save ? CommonResult.success() : CommonResult.fail();
    }


    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/getPort")
    public String getPort() {
        return "springCloud with OpenFeign eureka：" + serverPort + " >>>> UUID= " + UUID.randomUUID().toString();
    }


    @GetMapping("/timeOut/{second}")
    public String timeOut(@PathVariable("second") int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "睡眠时间：" + second;
    }


    /**
     * 当指定服务调用超时时自动降级
     * 自动降级仅对添加了@HystrixCommand注解的方法有效
     * 并在注解内指定 @HystrixProperty注解配置，指定超时时间对应的值和类
     * 此处指定超时3秒后调用备用方法
     * <p>
     * 此种方法比较麻烦，每个方法都需要单独指定降级方案（一般调用者不采用，仅被调用者用来做处理）
     *
     * @param second 超时时间
     * @return 休眠经过的时间或降级处理后的文字
     */
    @HystrixCommand(fallbackMethod = "timeOutSingleFallBack", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    @GetMapping("/timeOutSingle/{second}")
    String timeOutSingle(@PathVariable("second") int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "timeOutSingle方法，睡眠时间：" + second;

    }

    /**
     * timeOutSingle方法调用超过3秒时被调用的降级方案
     *
     * @param second 超时时间
     * @return 休眠经过的时间或降级处理后的文字
     */
    String timeOutSingleFallBack(@PathVariable("second") int second) {
        return "payment端=====timeOutSingle方法的超时FallBack结果,timeOutSingle调用服务的时间超过3秒，调用时指定休眠的值为：" + second;
    }


    /**
     * 当指定服务调用超时或异常时自动降级（默认1秒）
     * 使用 @HystrixCommand注解，使用其调用备用的默认方法处理
     * 注：需要在本类上添加@DefaultProperties注解
     * 并在注解中使用defaultFallback = "方法名" 指定默认备用方法的名称
     * <p>
     * 此种方法相对于上一种，不需要单独为每个方法调用异常时的降级方案（一般调用者不采用，仅被调用者用来做处理）
     * 调用者的最佳方案为：在调用的远程服务接口内做处理，统一规定降级方案
     *
     * @param second 超时时间
     * @return 休眠经过的时间或降级处理后的文字
     */
    @HystrixCommand
    @GetMapping("/timeOutGlobal/{second}")
    String timeOutGlobal(@PathVariable("second") int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "timeOutGlobal方法，睡眠时间：" + second;

    }

    /**
     * 默认的调用异常或超时的降级方案
     * 方法上添加了@HystrixCommand注解的，调用异常时会使用该方法
     * 注：需要在本类上添加@DefaultProperties注解
     * 并在注解中使用defaultFallback = "方法名" 指定默认备用方法的名称
     */
    String defaultGlobalFallBack() {
        return "payment端=====默认的超时FallBack结果";
    }



      /*
        服务降级和熔断的区别：

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

     */


    /**
     * 服务熔断：
     *
     * 使用@HystrixCommand注解
     *      在注解内使用fallbackMethod= 指定熔断后触发降级调用的方法
     *      使用commandProperties= 指定触发熔断的条件
     *          在使用commandProperties内使用@HystrixProperty注解指定条件和值
     *          使用name属性指定条件名，使用value属性指定值
     *          name                                        value
     *          circuitBreaker.enabled                      boolean     是否开启断路器
     *          circuitBreaker.sleepWindowInMilliseconds    int         时间窗口期（毫秒）
     *          circuitBreaker.requestVolumeThreshold       int         请求次数
     *          circuitBreaker.errorThresholdPercentage     int         失败的阈值（百分比）
     *
     * 指定后，当请求在时间窗口期内的失败或超时的次数超过了失败阈值，就会在一定时间内触发熔断，之后的访问请求，都会被降级
     * 此处使用传入参数的值的正负模拟成功和失败
     * 若为正数则正常执行，若为0或负整数则抛出异常执行降级方法；
     * 当指定时间内失败次数达到阈值，就是触发熔断，之后一段时间内，即使传入正数也会直接执行降级的备用方法
     * 熔断一定时间后，hystrix会慢慢尝试将少量请求正常放行不熔断，若发现该服务可用后，则会取消该服务的熔断
     *
     * 注：
     * 服务熔断的开启或关闭有默认的触发条件：
     *
     * 1.当请求满足一定阈值（默认10秒内超过20个请求）
     * 2.当失败率满足一定阈值（默认10秒内失败率超过50%）
     * 3.满足以上条件，服务会触发熔断，
     * 4.熔断后，所有的请求都不会被转发（不会调用目标方法，都会被降级调用备用方法或直接返回）
     * 5.一段时间之后（默认5秒），熔断的断路器会处于半开状态，会让其中一个进来的请求进行转发（正常执行调用方法）
     *   如果成功，断路器会关闭，熔断接触，否则不断继续4-5步
     *
     *
     *
     *
     * @param num 传入的数字，若为正数正常调用，0或负数抛出异常（降级转到paymentCircuitBreakerFallback方法）
     * @return  异常或正常调用信息
     */
    @HystrixCommand(fallbackMethod = "paymentCircuitBreakerFallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),   //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),  //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),    //时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),    //失败率达到多少后跳闸
    })
    @GetMapping("breaker/{num}")
    public String circuitBreaker(@PathVariable("num") Integer num) {
        if (num <= 0) {
            throw new RuntimeException("******id 不能为负数");
        }
        String serialNumber = UUID.randomUUID().toString();
        return Thread.currentThread().getName() + "\t" + "CircuitBreaker调用成功，流水号：" + serialNumber;
    }

    public String paymentCircuitBreakerFallback(@PathVariable("num") Integer num) {
        return "传入的数字为" + num + " \n 此处数字应为负数或0，若数字为正数说明触发了熔断";
    }


}


package com.example.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * sentinel异常熔断降级
 *
 * 限流和异常的处理分别使用@SentinelResource内不同的属性指定，此处测试异常导致的熔断降级
 *
 *
 *
 * @author booty
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    /**
     * 测试服务产生异常熔断降级和限流的区别
     *
     * 注解@SentinelResource：
     *  value：sentinel端添加的热点限流规则时指定的名称（唯一，一般与路径相同，也可以不指定直接使用访问路径）
     *  blockHandler ： 限流后的处理方法（若不指定会默认返回抛出BlockException（限流异常）的错误页面，建议指定）
     *                  注：该方法仅会处理限流后会抛出的异常BlockException，其他异常不会进入该方法处理
     *  fallback ： 产生异常的处理方法
     *      注：
     *      blockHandler和fallback的区别：
     *          blockHandler    只针对sentinel在控制台配置的规则违规做处理（控制台配置的限流熔断规则）
     *          fallback ： 产生异常的处理方法
     *              注：
     *           blockHandler和fallback的区别：
     *                  blockHandler    只针对sentinel在控制台配置的规则违规做处理（控制台配置的限流熔断规则，只处理BlockException）
     *                  fallback        只针对业务内逻辑异常（处理运行时异常）
     *           exceptionsToIgnore : []   忽略的异常，指定的异常不会被fallback处理
     *              两者分别处理两种不同的错误返回页面
     *              若同时配置，控制台配置的限流熔断降级抛出的异常会走blockHandler，业务异常走fallback
     *              若仅配置fallback，sentinel在控制台配置的规则违规做处理也会走fallback
     *
     *  注：
     *            blockHandler指定的方法接收的参数必须要有BlockException，否则无法生效
     *            fallback指定的方法接收的参数必须要由Throwable，否则无法生效
     *
     * @param num 传入数字
     * @return 端口+数字
     */

    @GetMapping("getNum/{num}")
    @SentinelResource(value = "num" ,fallback = "numFallback",blockHandler = "numBlockHandler")
    String getNum(@PathVariable("num") int num) {
        if (num <= 4) {
            return "payment端口：" + serverPort + " ====传入数字为：" + num;
        } else {
            throw new RuntimeException("数字不能大于4");
        }
    }

    String numFallback(int num){
        return "numFallback,业务处理异常，传入数字不能大于4，传入数字为："+num;
    }

    String numBlockHandler(int num){
        return "numBlockHandler,控制台配置规则生效，产生限流或熔断，传入数字为："+num;
    }


}


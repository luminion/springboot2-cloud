package com.example.controller;

import com.example.feignService.PaymentFeignService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author booty
 * @version 1.0
 */
@RestController
@RequestMapping("order")
public class OrderController {


    @Resource
    private PaymentFeignService paymentFeignService;

    /**
     * 调用payment服务，若传入数字大于4则抛异常，小与4正常执行
     *
     * @param num 传入数字
     * @return 异常或端口+数字
     */
    @GetMapping("/getNum/{num}")
    public String paymentInfo(@PathVariable("num") int num) {
        return paymentFeignService.getNum(num);
    }



}

package com.example.controller;


import com.example.service.OrderService;
import com.example.service.PaymentService;
import entity.CommonResult;
import entity.Order;
import entity.Payment;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author booty
 */
@RestController
public class OrderController {
    @Resource
    OrderService orderService;
    @Resource
    PaymentService paymentService;

    @GlobalTransactional
    @GetMapping("/add/{num}")
    public boolean get(@PathVariable("num") int num) {
        orderService.save(new Order().setName("测试" + LocalDate.now().toString()));
        paymentService.add(num);
        return true;
    }


}


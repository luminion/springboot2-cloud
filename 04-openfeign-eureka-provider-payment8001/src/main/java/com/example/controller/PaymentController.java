package com.example.controller;


import com.example.service.PaymentService;
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
 */
@RestController
@RequestMapping("/payment")
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
    public String timeOut(@PathVariable("second") int second){
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "睡眠时间："+second;
    }

}


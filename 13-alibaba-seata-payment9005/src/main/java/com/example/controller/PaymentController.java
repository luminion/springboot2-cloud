package com.example.controller;


import com.example.service.PaymentService;
import entity.CommonResult;
import entity.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author booty
 */
@RestController
public class PaymentController {
    @Resource
    PaymentService paymentService;

    @GetMapping("/add/{num}")
    public boolean add(@PathVariable("num") int num) {

        if (num>1){
            throw new RuntimeException();
        }else {
            paymentService.save(new Payment().setSerial(LocalDate.now().toString()));
        }
        return true;

    }



}


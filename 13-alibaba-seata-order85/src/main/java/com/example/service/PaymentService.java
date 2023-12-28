package com.example.service;

import entity.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author booty
 * @version 1.0
 */
@FeignClient("provider-payment")
public interface PaymentService {

    /**
     * 调取服务
     *
     * @param num 传入参数
     * @return 返回陈工
     */
    @GetMapping("/add/{num}")
    Boolean add(@PathVariable("num") int num);

}

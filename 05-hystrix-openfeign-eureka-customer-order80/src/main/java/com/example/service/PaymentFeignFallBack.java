package com.example.service;

import org.springframework.stereotype.Component;

/**
 * 服务降级处理的类
 * 注：
 * 需要添加@Component注解，否则spring扫描到该bean进行注入
 *
 * @author booty
 * @version 1.0
 * @date 2021/3/20 11:28
 */
@Component
public class PaymentFeignFallBack implements PaymentFeignService {
    final static String MESSAGE = "order端====调用payment服务超时或异常,此为PaymentFeignFallBack的服务降级响应";

    @Override
    public String getPort() {
        return MESSAGE;
    }

    @Override
    public String timeOut(int second) {
        return MESSAGE;
    }
}

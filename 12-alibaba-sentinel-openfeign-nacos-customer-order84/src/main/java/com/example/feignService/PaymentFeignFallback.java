package com.example.feignService;

import org.springframework.stereotype.Component;

/**
 * 远程调用降级类
 *
 * @author booty
 * @version 1.0
 */
@Component
public class PaymentFeignFallback implements PaymentFeignService {
    @Override
    public String getNum(int num) {
        return "调用失败的降级返回";
    }
}

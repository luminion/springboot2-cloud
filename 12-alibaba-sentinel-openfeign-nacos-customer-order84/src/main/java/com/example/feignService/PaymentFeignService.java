package com.example.feignService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * payment服务调用
 *
 * @author booty
 * @version 1.0
 */
@FeignClient(value = "nacos-payment-provider",fallback = PaymentFeignFallback.class)
public interface PaymentFeignService {

    /**
     * 获取payment的端口\
     *
     * @param num 传入的数字
     * @return 端口号
     */
    @GetMapping("/payment/getNum/{num}")
    String getNum(@PathVariable("num")int num);

}

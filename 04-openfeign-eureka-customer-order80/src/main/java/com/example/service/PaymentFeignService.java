package com.example.service;

import com.example.controller.OrderController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



/**
 * openFeign远程调用接口
 * 此处@Component可以不加，一样会被扫描到
 *
 * 注解@FeignClient指定对应服务注册名
 * 此处注解必须与服务在注册中心的注册名相同
 *
 * 对参数传递的要求：
 * https://blog.csdn.net/weixin_44257627/article/details/104212985?utm_medium=distribute.pc_relevant.none-task-blog-baidujs_baidulandingword-1&spm=1001.2101.3001.4242
 *  尽量使用@RequestParam注解，若为@RequestBody,则请求类型必须时post
 *
 * @author booty
 * @version 1.0
 */
@Component
@FeignClient(OrderController.PAYMENT_REG_NAME)
public interface PaymentFeignService {


    /**
     * 获取payment服务运行的端口和一个uuid
     * @return 端口+uuid
     */
    @GetMapping("payment/getPort")
    String getPort() ;


    /**
     * 测试服务超时，payment服务将会休眠几秒，若时间大于设置的等待响应时间就会超时
     *
     * 此处若不适用restful风格，传递参数时，需要使用@RequestParam注解（或使用post请求，用@RequestBody注解）
     * 否则远程服务容易出现接收不到参数的问题
     *
     * @param second 休眠的时间
     * @return  异常或 休眠了几秒
     */
    @GetMapping("payment/timeOut/{second}")
    String timeOut(@PathVariable("second") int second);
}

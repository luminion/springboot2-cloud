package com.example.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * 自定义限流业务处理逻辑类
 *
 * 方法中需要传入BlockException，否则会报错
 *
 * @author booty
 * @version 1.0
 */
public class CustomerBlockHandler {
    public static String handlerException(BlockException exception){
        return "自定义限流，global handlerException-----1";
    }
    public static String handlerException2(BlockException exception){
        return "自定义限流，global handlerException-----2";
    }
}

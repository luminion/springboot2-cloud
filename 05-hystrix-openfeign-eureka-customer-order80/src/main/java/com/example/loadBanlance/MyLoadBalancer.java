package com.example.loadBanlance;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * 自定义loadBalance负载均衡算法类
 * 启用时单独启动指定方法，并需要将config配置类中的@LoadBalanced注解去掉
 * 以确实使用的是自己定义的规则
 *
 * @author booty
 * @version 1.0
 * @date 2021/3/19 14:28
 */
public interface MyLoadBalancer {

    /**
     * 传入服务器实例列表，通过算法选择一个服务器调用
     *
     * @param serviceInstanceList  服务器列表
     * @return 被调用的服务器
     */
    ServiceInstance getWhichInstance(List<ServiceInstance> serviceInstanceList);
}

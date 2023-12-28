package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * 注解@EnableDiscoveryClient
 * 用于想使用consul或者zookeeper作为注册中心时注册服务
 *
 * windows安装zooKeeper
 * https://blog.csdn.net/qq_33316784/article/details/88563482
 *
 * zooKeeper基础
 * https://blog.csdn.net/u011863024/article/details/107434932
 *
 *
 * zooKeeper的注册节点使用的默认是临时节点，不同于eureka，当其zooKeeper在指定时间内检测不到心跳时，会立刻删除注册的节点
 *
 *
 * @author booty
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ZooKeeperPayment8004 {
    public static void main(String[] args) {
        SpringApplication.run(ZooKeeperPayment8004.class, args);
    }
}

package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * 注解@EnableDiscoveryClient
 * 用于想使用consul或者zookeeper作为注册中心时注册服务
 *
 * windows安装运行consul
 * 进入官网下载安装包，之后解压，在解压目录打开cmd，输入指令：
 * consul -version      查看版本
 * consul agent -dev    运行
 *
 * consul基础
 * https://blog.csdn.net/bbwangj/article/details/81116505
 *
 * consul同zooKeeper,当检测到注册的服务下线时，会立即删除该服务
 *
 *
 * CAP概念：
 * Consistency          强一致性
 * Availability         高可用性
 * Partition tolerance  分区容错性
 *
 * 一个分布式系统只能满足其中两项内容
 * 其中P（分区容错性）一般是必须的，所以分布式系统又分为 CP 和 AP 两种类型
 * 系统对应的类型：
 * AP:  eureka
 * CP:  redis;zooKeeper;consul
 * CA:  mysql
 *
 *
 *
 * @author booty
 * @version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ConsulPayment8006 {
    public static void main(String[] args) {
        SpringApplication.run(ConsulPayment8006.class, args);
    }
}

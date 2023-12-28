package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注解：@RefreshScope:
 * SpringCloud原生注解 支持Nacos的动态刷新功能
 *
 * @author booty
 * @version 1.0
 */
@RestController
@RefreshScope
public class ClientConfigController {
    /**
     * 获取nacos服务器中的配置文件中的值（需要自己在nacos中定义）
     *
     * 因nacos默认是以集群方式启动，此处将其修改为单机
     *  nacos下载后要修改配置文件，startup.cmd或startup.sh；
     *  1.修改项：
     *              rem set MODE="cluster"
     *          改为：
     *              set MODE="standalone"
     *          参考链接：
     *              https://blog.csdn.net/lyxuefeng/article/details/108752302
     *  2.或使用命令：
     *      cmd startup.cmd -m standalone
     *      sh startup.sh -m standalone
     *      bash startup.sh -m standalone（ubuntu系统）
     *
     * 启动后：
     * localhost:8848/nacos/
     * 使用上述网址登录nacos查看情况（默认账号密码nacos）
     * 并新建与本项目项目名匹配规则的配置文件名（见bootstrap.yml），在配置文件中指定：
     * name: admin
     *
     * 访问链接调用方法即可获取到配置文件中的信息
     *
     * 类上添加了@RefreshScope注解时
     * 当在nacos中修改配置文件值的时候会自动刷新配置的属性的值
     *
     *
     *
     *
     */
    @Value("${name}")
    String name;

    @GetMapping("/name")
    public String name(){
        return name;
    }

}

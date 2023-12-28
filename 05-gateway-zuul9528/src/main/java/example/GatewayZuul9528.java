package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 网关请求转发模块
 * 该模块从eureka注册中心获取服务名字
 * 收到请求后
 * 根据配置文件（或配置类）中的映射规则将请求转发到对应服务处理
 *
 * 注解：@EnableZuulProxy 开启zuul网关代理
 *
 *
 * @author booty
 * @version 1.0
 */
@EnableEurekaClient
@SpringBootApplication
@EnableZuulProxy
public class GatewayZuul9528 {
    public static void main(String[] args) {
        SpringApplication.run(GatewayZuul9528.class, args);
    }

}

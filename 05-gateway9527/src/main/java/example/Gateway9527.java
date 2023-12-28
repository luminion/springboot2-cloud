package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 网关请求转发模块
 * 该模块从eureka注册中心获取服务名字
 * 收到请求后
 * 根据配置文件（或配置类）中的映射规则将请求转发到对应服务处理
 *
 * 若开启了redis限流，需要打开redis
 *
 * @author booty
 * @version 1.0
 */
@EnableEurekaClient
@SpringBootApplication
public class Gateway9527 {
    public static void main(String[] args) {
        SpringApplication.run(Gateway9527.class, args);
    }

}

package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * spring集成的消息中间件的组件Stream
 *
 * 消息接收者模块
 * 目前支持rabbitmq和kafka
 * 使用服务不同导入的依赖也不同
 * 此处使用rabbitmq
 *
 * @author booty
 * @version 1.0
 */
@EnableEurekaClient
@SpringBootApplication
public class StreamCustomer8802 {
    public static void main(String[] args) {
        SpringApplication.run(StreamCustomer8802.class, args);
    }

}

package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 获取配置中心的值的模块
 * 需要添加配置中心的依赖
 *
 * @author booty
 * @version 1.0
 */
@EnableEurekaClient
@SpringBootApplication
public class ConfigClient3366 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClient3366.class, args);
    }

}

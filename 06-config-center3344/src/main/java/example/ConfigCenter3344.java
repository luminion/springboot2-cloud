package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 统一配置中心
 * 注解@EnableConfigServer
 * 开启统一配置中心
 * 需要在配置文件中指定git仓库地址和文件路径
 * 可以通过在浏览器中输入指定地址和文件名访问远程仓库中的文件，如：
 * http://localhost:3344/master/config-dev.properties
 * http://项目路径/git分支名/远程仓库中的文件名
 *
 *
 *
 * @author booty
 * @version 1.0
 */
@EnableEurekaClient
@SpringBootApplication
@EnableConfigServer
public class ConfigCenter3344 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigCenter3344.class, args);
    }

}

package example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注解@RefreshScope，
 * 需要在配置文件中配置暴露监控端点才能生效（需要actuator监控依赖）
 * 当修改远程配置中心的配置文件后，外部手动发送一条post请求即可实现该类的配置文件刷新：
 * http://localhost：3355/actuator/refresh
 * 此时该类配置文件的值就会刷新
 *
 * 可以通过控制台发送post请求：
 * curl -X POST http://localhost:3355/actuator/refresh
 *
 * @author booty
 * @version 1.0
 */
@RestController
@RefreshScope
public class ConfigController {
    /**
     * 获取配置中心的配置文件的内容
     * 注：此处的指定的属性，必须是配置文件中有的键
     * 配置中心文件内容：（文件类型：properties或yaml、yml都行）
     * name=admin
     * password=123
     * num=123456
     * cmd.refresh=curl -X POST http://localhost:3355/actuator/refresh
     *
     */
    @Value("${name}")
    String name;
    @Value("${password}")
    String password;
    @Value("${num}")
    int num;
    @Value("${cmd.refresh}")
    String code;

    @GetMapping("getInfo")
    public String getConfigInfo(){
        return "----从配置中心获取的name属性="+name+
                ";----从配置中心获取的password属性="+password+
                ";----从配置中心获取的num属性num="+num+
                ";----从配置中心获取的cmd刷新配置文件值的cmd命令行命令："+code;
    }



}

package com.example.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author booty
 * @version 1.0
 */
@Configuration
public class Config {

    /**
     * 添加restTemplate到spring 容器中
     * 用于向其他服务发送请求
     * @return RestTemplate
     */

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


    /**
     * 负载均衡的规则
     * 系统默认为轮询，可以通过修改注入容器的iRule的实现类来修改规则
     * 例如：RandomRule，RoundRobinRule RetryRule
     *
     * @return IRule
     */
    @Bean
    public IRule iRule(){
        return new RoundRobinRule();
    }

}

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
     * 系统默认为轮询，可以通过修改注入容器的iRule的实现类来修改规
     *
     * 轮询负载均衡算法原理：
     * rest请求第几次请求%服务器集群总数量=实际调用服务器下标  （相除取余数）
     * 每次重启rest接口后从1开始计算
     * <p>
     * 注：
     * 若想配置的该项仅对目前客户端生效
     * 所在的自定义配置类不能放在@ComponentScan注解所扫描的包及子包下
     * 主启动类的@SpringBootApplication注解包含了@ComponentScan注解，所以配置类需要创建到与主启动类不同的包下
     * 否则会被所有客户端共享，达不到特殊化定制当前客户端的目的
     * 步骤：
     * 新建不同于主启动所在包的同级包
     * 新建配置类添加@Configuration注解
     * 在配置类中添加规则的方法并添加@Bean注解
     * 在主启动类上使用@RibbonClient注解
     * 在注解中指定访问的服务名称和对应配置类（名字要注意大小写，与发送请求时使用的名字一致，配置类名和方法名不要相同）
     * 例：@RibbonClient(name = "PROVIDER-PAYMENT",configuration = MyOwnIRule.class)
     * <p>
     * <p>
     * com.netflix.loadbalancer.RoundRobinRule                  轮询
     * com.netflix.loadbalancer.RandomRule                      随机
     * com.netflix.loadbalancer.RetryRule                       先按照RoundRobinRule获取服务，若在指定时间内失败会进行重试，获取可用的服务
     * com.netflix.loadbalancer.WeightedResponseTimeRule        对RoundRobinRule的扩展，响应速度越快，权重越大，越容易被选择
     * com.netflix.loadbalancer.BestAvailableRule               会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量小的服务
     * com.netflix.loadbalancer.AvailabilityFilteringRule       先过滤掉故障服务，再选择一个并发量较小的服务
     * com.netflix.loadbalancer.ZoneAvoidanceRule               默认规则，复合判断服务器所在区域的性能和服务器的可用性，然后选择服务器
     *
     * @return IRule
     */
    @Bean
    public IRule iRule(){
        return new RoundRobinRule();
    }



}

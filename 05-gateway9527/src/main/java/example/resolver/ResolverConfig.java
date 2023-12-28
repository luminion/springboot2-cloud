package example.resolver;


import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

/**
 * 注册限流器规则的的配置类（需要添加依赖并开启限流规则）
 * 也可以通过实现KeyResolver的resolve接口新建类并使用@bean注册
 * 此处使用lambda表达式简写，需要在配置文件中指定限流器的名字
 * 注意：requestRateLimiterGatewayFilterFactory装配时要求为单例，此处注册时只能注册一个bean
 *
 *
 * 在配置文件配置redis的信息，并配置RequestRateLimiter的限流过滤器，该过滤器需要配置三个参数：
 *
 * burstCapacity，令牌桶总容量。
 * replenishRate，令牌桶每秒填充平均速率。
 * key-resolver，用于限流的键的解析器的 Bean 对象的名字。它使用 SpEL 表达式根据#{@beanName}从 Spring 容器中获取 Bean 对象。
 *
 * @author booty
 * @version 1.0
 */
@Configuration
public class ResolverConfig {

    /**
     * 根据hostAddress进行限流
     * @return KeyResolver
     */

    public KeyResolver hostAddrKeyResolver(){
        return e -> Mono.just(e.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }

    /**
     * 根据访问的uri地址进行限流
     * @return KeyResolver
     */
    @Bean
    public KeyResolver uriKeyResolver(){
        return e -> Mono.just(e.getRequest().getURI().getPath());
    }

    /**
     * 根据用户进行限流
     * @return KeyResolver
     */
    public KeyResolver userKeyResolver(){
        return e -> Mono.just(e.getRequest().getQueryParams().getFirst("user"));
    }


}
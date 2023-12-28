package example.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

/**
 * 通过配置类自定义网关
 * （该种配置方式较复杂，不常用，了解即可，一般在配置文件中指定）
 *
 * @author booty
 * @version 1.0
 */
@Configuration
public class Config {



    /**
     * 问题 feign.codec.EncodeException:
     * Spring Cloud Gateway是基于WebFlux的，没有json转换器，返回json时会报该异常
     * 是ReactiveWeb，所以HttpMessageConverters不会自动注入。
     * 在gateway服务中配置以下Bean，即可解决。
     *
     * @param converters converters 注入转换器
     * @return HttpMessageConverters 添加json转换器的bean
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }



    /**
     * 生成自定义的路由规则
     * 访问uri带了guonei，就会跳转到百度国内新闻
     * 访问uri带了guoji，就会跳转到百度国际新闻
     *
     * @param routeLocatorBuilder 路由映射生成器
     * @return 路由规则
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();

        routes.route("path_route_route1",
                r -> r.path("/guoji")
                        .uri("http://news.baidu.com/guoji")).build();

        routes.route("path_route_route2",
                r -> r.path("/guonei")
                        .uri("http://news.baidu.com/guonei")).build();

        return routes.build();
    }



}


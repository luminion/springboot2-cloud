package example.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * 自定义全局过滤器
 * 需要实现GlobalFilter接口，并添加@Component让spring扫描到
 * 实现Ordered接口是为了让该过滤器的优先级更高 getOrder方法返回值约小，优先级越高
 *
 *
 * @author booty
 * @version 1.0
 */
@Slf4j
@Component
public class MyFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入自定义全局过滤器: "+new Date());
//        //每次进来后判断参数带不带uname这个key
//        String uname = exchange.getRequest().getQueryParams().getFirst("uname");
//        if(uname == null){
//            log.info("*********用户名为null ，非法用户，o(╥﹏╥)o");
//            //uname为null非法用户，禁止继续访问
//            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
//            return exchange.getResponse().setComplete();
//        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

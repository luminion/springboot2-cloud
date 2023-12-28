package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * 注解@EnableHystrixDashboard 开启仪表盘监控
 *
 * 被监控的项目/模块需要有健康检查的依赖：
 *          <dependency>
 *             <groupId>org.springframework.boot</groupId>
 *             <artifactId>spring-boot-starter-actuator</artifactId>
 *         </dependency>
 *
 * 被监控的项目/模块还需要添加一个组件到容器中：
 *
 *      此配置是为了服务监控而配置，与服务容错本身无关,SpringCloud升级后的坑
 *      ServletRegistrationBean因为springboot的默认路径不是"/hystrix.stream"，
 *      只要在自己的项目里配置上下面的servlet并使用@Bean注解注入就可以了
 *
 *     public ServletRegistrationBean getServlet(){
 *         HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
 *         ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
 *         registrationBean.setLoadOnStartup(1);
 *         registrationBean.addUrlMappings("/hystrix.stream");
 *         registrationBean.setName("HystrixMetricsStreamServlet");
 *         return registrationBean;
 *     }
 *
 * 仪表盘的访问地址：（本项目的地址加上/hystrix）
 * http://localhost:9001/hystrix
 *
 * 进入仪表盘，之后仪表盘中输入要监控的服务的地址+hystrix.stream进行监控
 * 如：
 * http://localhost:8001/hystrix.stream
 *
 * 向被监控服务器发送请求使其触发熔断，否则显示的页面会一直显示loading
 *
 * @author booty
 * @version 1.0
 */
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashBoard9001 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashBoard9001.class, args);
    }


}

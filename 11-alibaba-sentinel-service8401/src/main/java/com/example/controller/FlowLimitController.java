package com.example.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.handler.CustomerBlockHandler;
import entity.CommonResult;
import entity.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于测试流量控制限流的类
 *
 * 需要启动nacos注册中心和sentinel
 *
 * 访问：（默认）
 * localhost:8080
 * 进入sentinel进行控制（账号名密码均为sentinel）
 *
 * 注：
 * sentinel是懒加载的，需要先发送几次请求才能显示出控制界面
 * 在控制台的 簇点链路下指定流控规则,添加常见：
 *
 * 阈值类型：
 *  QPS：
 *      设定每秒最大访问数的阈值，超过后会提示：Blocked by Sentinel (flow limiting)
 *  线程数：
 *      设定能同时运行的最大线程数，超过后会提示：Blocked by Sentinel (flow limiting)
 *      （即：同个方法所能同时运行的最大下次你哼）
 *      （例如，某个controller方法需要运行3秒，设定的线程数为2，此时如果controller已经有两个请求还未执行完成，那么其余请求会被拦截）
 *      （当正在执行的线程执行完后，此时下一个请求才能进入该controller）
 *
 * 流控模式：
 *   直接：
 *      接口达到限流条件时，直接限流
 *   关联：
 *      当关联的资源达到阈值时，就限流自己
 *      （实际应用例如：支付模块繁忙时，让订单模块暂缓下单）
 *   链路：
 *      只记录指定链路上的流量（指定资源从入口资源进来的流量，如果达到阈值，就可以限流）[api级别的针对来源]
 *
 * 流控效果：
 *  快速失败：
 *      直接失败
 *  Warm Up：
 *      即请求 QPS 从 threshold / 3 开始，经预热时长逐渐升至设定的 QPS 阈值（阈值刚开始为3，之后慢慢提升至指定阈值）
 *  排队等待：
 *      QSP模式才行，请求依次排队进入，默认值阈值为毫秒
 *
 * 熔断降级：
 *  RT：
 *     平均响应时间（毫秒）超出阈值（毫秒）且在窗口时间内的请求QPS>=5（每秒至少5次请求），触发后降级，时间窗口期后关闭断路器（恢复正常）
 *     （RT阈值最大4900，更大需要通过-Dcsp.sentinel.statistic.max.rt=xxx设置，时间窗口期单位为秒）
 *  异常比例：
 *      QPS>=5（每秒至少5次请求）,且异常比例（秒级统计）大于阈值时，触发降级；时间窗口期后关闭断路器（恢复正常）
 *      （异常比例范围：[0.0，1.0],代表0%-100%，时间窗口期单位为秒）
 *  异常数：
 *      时间窗口期内出现的异常数量（分钟统计）超过阈值时，触发降级；时间窗口期后关闭断路器（恢复正常）
 *      （注：由于统计是分钟级别的，时间窗口期单位也为秒，若时间窗口期小于60秒则可能会一直触发熔断，不会恢复，所以时间窗口期一般大于60）
 * （注：sentinel的断路器不同于hystrix，没有半开状态，只有时间窗口期后才会恢复）
 *
 *
 *
 * 热点参数限流：
 *      热点参数限流会统计传入参数中的热点参数，并根据配置的限流阈值与模式，对包含热点参数的资源调用进行限流。
 *      热点参数限流可以看做是一种特殊的流量控制，仅对包含热点参数的资源调用生效。
 *      注：仅支持QPS阈值
 *      （例：比如：1.商品 ID 为参数，统计一段时间内最常购买的商品 ID 并进行限制 ；2.用户 ID 为参数，针对一段时间内频繁访问的用户 ID 进行限制）
 *   sentinel端设置参数：
 *      参数索引        对应方法传入参数的下标（从0开始）
 *      阈值           访问数量
 *      统计时长        时间周期
 *      （指定后，当调用该请求对应的方法且传入了参数索引指定的参数时，在统计时长内达到了阈值就会进行限流，直到统计时长结束）
 *      参数例外项
 *          当传入的参数的值为特殊值时限流的规则单独指定阈值
 *          注：只支持String和基本数据类型
 *          （需要指定：参数类型、参数值、阈值）
 *          （例如：下载传入的userid，vip用户下载数量和普通用户不同，根据userid分辨是否能继续下载）
 *          服务器端的配置见本类testHotKey()方法
 *      注解@SentinelResource：
 *          value：sentinel端添加的热点限流规则时指定的名称（唯一，一般与路径相同，也可以不指定直接使用访问路径）
 *          blockHandler ： 限流后的处理方法（若不指定会默认返回抛出BlockException（限流异常）的错误页面，建议指定）
 *                     注：该方法仅会处理限流后会抛出的异常BlockException，其他异常不会进入该方法处理
 *          fallback ： 产生异常的处理方法
 *          exceptionsToIgnore : []   忽略的异常，指定的异常不会被fallback处理
 *              注：
 *              blockHandler和fallback的区别：
 *                  blockHandler    只针对sentinel在控制台配置的规则违规做处理（控制台配置的限流熔断规则，只处理BlockException）
 *                  fallback        针对业务内逻辑异常（处理所有异常）
 *              两者分别处理两种不同的错误返回页面
 *              若同时配置，控制台配置的限流熔断降级抛出的异常会走blockHandler，业务异常走fallback
 *              若仅配置fallback，sentinel在控制台配置的规则违规做处理也会走fallback
 * 注：
 *    blockHandler指定的方法接收的参数必须要有BlockException，否则无法生效
 *    fallback指定的方法接收的参数必须要由Throwable，否则无法生效
 *
 * 资源名称限流：
 *      对使用了@SentinelResource内指定了名称的资源进行限流
 *
 * url限流：
 *      对使用了@SentinelResource内指定了名称的资源进行限流（在sentinel上添加访问路径，其他同资源名）
 *
 *
 * 系统自适应限流：
 *      根据系统的硬件和运行情况自动进行限流
 *      规则：
 *          LOAD        仅对linux和unix机器生效，当系统load值超过设定值，且并发线程数超过系统估算容量（maxQPS * minRT）时限流，估算容量参考值是 CPU数量 * 2.5
 *          RT          响应时间超过阈值（毫秒）限流
 *          线程数       线程超过阈值限流
 *          入口QPS      当单台机器所有入口流量达到阈值触发限流（一般不建议使用，指定过小容易出问题）
 *          CPU使用率    超过CPU使用率后限流，取值[0.0,1.0]
 *
 *  sentinel的3个核心API
 *      SphU        定义资源
 *      Tracer      定义统计
 *      ContextUtil 定义上下文
 * @author booty
 * @version 1.0
 */
@RestController
@Slf4j
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA(){
        return "--------testA";
    }

    @GetMapping("/testB")
    public String testB(){
        return "--------testB";
    }

    @GetMapping("/testE")
    public String testE(){
        log.info("testE测试异常数");
        int age = 10/0;
        return "--------testE ";
    }


    /**
     * 测试热点限流（仅支持QPS模式）
     *
     *      热点参数限流会统计传入参数中的热点参数，并根据配置的限流阈值与模式，对包含热点参数的资源调用进行限流。
     *      热点参数限流可以看做是一种特殊的流量控制，仅对包含热点参数的资源调用生效。
     *      注：仅支持QPS阈值
     *      （例：比如：1.商品 ID 为参数，统计一段时间内最常购买的商品 ID 并进行限制 ；2.用户 ID 为参数，针对一段时间内频繁访问的用户 ID 进行限制）
     *   sentinel端设置参数：
     *      参数索引        对应方法传入参数的下标（从0开始）
     *      阈值           访问数量
     *      统计时长        时间周期
     *      （指定后，当调用该请求对应的方法且传入了参数索引指定的参数时，在统计时长内达到了阈值就会进行限流，直到统计时长结束）
     *      参数例外项
     *          当传入的参数的值为特殊值时限流的规则单独指定阈值
     *          注：只支持String和基本数据类型
     *          （需要指定：参数类型、参数值、阈值）
     *          （例如：下载传入的userid，vip用户下载数量和普通用户不同，根据userid分辨是否能继续下载）
     *
     * 注解@SentinelResource：
     * value：sentinel端添加的热点限流规则时指定的名称（唯一，一般与路径相同，也可以不指定直接使用访问路径）
     * blockHandler ： 限流后的处理方法（若不指定会默认返回抛出BlockException（限流异常）的错误页面，建议指定）
     *                注：该方法仅会处理限流后会抛出的异常BlockException，其他异常不会进入该方法处理
     * fallback ： 产生异常的处理方法（详情见项目12-alibaba-sentinel-openfeign-nacos-provider-payment9003）
     * exceptionsToIgnore : []   忽略的异常，指定的异常不会被fallback处理
     *       注：
     *           blockHandler和fallback的区别：
     *               blockHandler    只针对sentinel在控制台配置的规则违规做处理（控制台配置的限流熔断规则）
     *               fallback        针对业务内的异常（运行时的产生的异常）
     *                  注：
     *                  blockHandler指定的方法接收的参数必须要有BlockException，否则无法生效
     *                  fallback指定的方法接收的参数必须要由Throwable，否则无法生效
     *
     *
     * 例如，若在sentinel指定了索引为0，阈值为1，统计时长为1，则调用该方法且传入了参数1时，每秒钟超过1次请求就会被限流
     *
     *
     * @param p1 参数1 非必须
     * @param p2 参数2 非必须
     * @return 字符串
     */
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "dealTestHotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2){
        return "testHotKey";
    }


    public String dealTestHotKey(String p1, String p2, BlockException exception){
        return "dealTestHotKey 传入的热点参数达到要求，已被限流";
    }



    /**
     * 按资源名称进行限流
     *
     * @return CommonResult
     */
    @GetMapping("/byResource")
    @SentinelResource(value = "byResource",blockHandler = "handlerException")
    public String byResource(){
        return "按资源名称限流测试OK";
    }

    /**
     * 按url进行限流
     * @return CommonResult
     */
    @GetMapping("/byUrl")
    @SentinelResource(value = "byResource",blockHandler = "handlerException")
    public String byUrl(){
        return "按url限流测试OK";
    }


    public String handlerException(BlockException exception){
        return "默认自定义的限流后的返回，服务触发限流";
    }


    /**
     * 自定的自定义限流业务处理
     *
     * 上述方法在处理时每次需要指定处理限流后的返回
     * 若多个controller和方法都需要书写，代码量大冗余高
     * 自定义一个类，用于处理限流的异常
     * 在@SentinelResource注解内指定类名，方法名:
     *      value = "限流的名称（按资源）",
     *      blockHandlerClass = "处理的类",
     *      blockHandler = "处理的方法"
     * 注：返回值要相同
     *
     * @return String
     */
    @GetMapping("/byOwnHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException2")
    public String byOwnHandler(BlockException exception){
        return "访问自定义限流限流业务处理，目前未被限流";
    }



    @Value("${server.port}")
    private String serverPort;

    /**
     * 测试服务产生异常熔断降级和限流的区别
     *
     * 注解@SentinelResource：
     *  value：sentinel端添加的热点限流规则时指定的名称（唯一，一般与路径相同，也可以不指定直接使用访问路径）
     *  blockHandler ： 限流后的处理方法（若不指定会默认返回抛出BlockException（限流异常）的错误页面，建议指定）
     *                  注：该方法仅会处理限流后会抛出的异常BlockException，其他异常不会进入该方法处理
     *  fallback ： 产生异常的处理方法
     *      注：
     *      blockHandler和fallback的区别：
     *          blockHandler    只针对sentinel在控制台配置的规则违规做处理（控制台配置的限流熔断规则，只处理BlockException）
     *          fallback        只针对业务内逻辑异常（处理运行时异常）
     *          exceptionsToIgnore : []   忽略的异常，指定的异常不会被fallback处理
     *      两者分别处理两种不同的错误返回页面
     *      若同时配置，控制台配置的限流熔断降级抛出的异常会走blockHandler，业务异常走fallback
     *      若仅配置fallback，sentinel在控制台配置的规则违规做处理也会走fallback
     *  注：
     *            blockHandler指定的方法接收的参数必须要有BlockException，否则无法生效
     *            fallback指定的方法接收的参数必须要由Throwable，否则无法生效
     *
     * @param num 传入数字
     * @return 端口+数字
     */

    @GetMapping("getNum/{num}")
    @SentinelResource(value = "num" ,fallback = "numFallback",blockHandler = "numBlockHandler")
//    @SentinelResource(value = "num" ,fallback = "numFallback")
//    @SentinelResource(value = "num" ,blockHandler = "numBlockHandler")
    String getNum(@PathVariable("num") int num) {
        if (num <= 4) {
            return "payment端口：" + serverPort + " ====传入数字为：" + num;
        } else {
            throw new RuntimeException("数字不能大于4");
        }
    }

    public String numFallback(int num,Throwable e){
        return "numFallback,业务处理异常，传入数字不能大于4，传入数字为："+num;
    }

    public String numBlockHandler(int num,BlockException exception){
        return "numBlockHandler,控制台配置规则生效，产生限流或熔断，传入数字为："+num;
    }


}

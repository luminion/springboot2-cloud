package example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * SpringStream用于接收中间件发送的消息的类
 *
 * 使用@EnableBinding注解将信道channel和exchange交换名称绑定在一起
 *
 * 注解：@EnableBinding注解接收一个参数，参数类型是class。
 * 传入的参数是“Source.class”,这是一个接口，
 * 定义了一个channel，为输出通道output。
 * “@EnableBinding(Source.class)”这整段代表创建Source定义的通道，并将通道和Binder绑定。
 *
 * Source接口是Spring Cloud Stream定义好的通道声明接口
 * 需要时，直接使用就好。类似还有Sink和Processor
 * Source       定义了输出通道
 * Sink         定义了输入通道
 * Processor    同时定义了输入和输出通道
 *
 * 详解文章链接：
 * https://blog.csdn.net/weixin_38399962/article/details/82192340
 *
 * @author booty
 * @version 1.0
 */
@Component
@EnableBinding(Sink.class)
public class ReceiveListenerController {

    @Value("${server.port}")
    private String serverPort;

    @StreamListener(Sink.INPUT)
    public void input(Message<String> message){
        System.out.println("消费者"+serverPort+"号，------->接收到的消息： "+message.getPayload());
    }

}

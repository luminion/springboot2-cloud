package example.service;

/**
 * 向消息中间见发送消息的类
 * @author booty
 * @version 1.0
 */
public interface IMessageProviderService {

    /**
     * 向消息中间件发送消息
     * @param msg 发送的消息
     */
    void send(String msg);
}

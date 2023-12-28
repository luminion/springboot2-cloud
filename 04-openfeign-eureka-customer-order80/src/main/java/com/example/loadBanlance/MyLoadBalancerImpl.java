package com.example.loadBanlance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义负载均衡实现类
 *
 * @author booty
 * @version 1.0
 */
@Component
@Slf4j
public class MyLoadBalancerImpl implements MyLoadBalancer {
    /**
     * 计数器，用于记录访问的次数
     * 此处使用原子类，避免多线程访问的累加数据不正确的问题
     */
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 获取当前的访问次数，并使访问次数加以
     *
     * @return 当前是第几次访问
     */
    public final int getAndIncrement() {
        int current;
        int next;
        do {
            //获取当前为第几次访问
            current = this.atomicInteger.get();
            //超过最大值，为0，重新计数 2147483647 Integer.MAX_VALUE，不直接写方法是因为方法运行时每次都会创建一个栈区，而写死就不会，涉及Jvm调优
            next = current >= 2147483647 ? 0 : current + 1;
            /*
             * 自旋锁
             * compareAndSet()比较获取的值是否被其他线程修改，
             * 若atomicInteger的值不等与current，则说明被修改了，返回false
             * 若未被修改，则作修改，使atomicInteger的值等于next，返回ture
             * 此处取反，设置成功则跳出循环，设置失败则重新设置
             */
        } while (!this.atomicInteger.compareAndSet(current, next));
        //执行到此处说明更改设置成功，本次访问为第N次，N的值就是next
        log.info("*****第几次访问，次数next：" + next);
        return next;
    }

    @Override
    public ServiceInstance getWhichInstance(List<ServiceInstance> serviceInstanceList) {
        //使用访问次数除以服务器个数 并取余数，获取每次访问的不同服务器
        int index = getAndIncrement() % serviceInstanceList.size();
        return serviceInstanceList.get(index);
    }
}

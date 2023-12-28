package com.example.service.impl;


import com.example.mapper.OrderMapper;
import com.example.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import entity.Order;
import entity.Payment;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author booty
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}

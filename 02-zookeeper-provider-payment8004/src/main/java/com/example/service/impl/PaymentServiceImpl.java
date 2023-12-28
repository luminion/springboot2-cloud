package com.example.service.impl;


import com.example.mapper.PaymentMapper;
import com.example.service.PaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

}

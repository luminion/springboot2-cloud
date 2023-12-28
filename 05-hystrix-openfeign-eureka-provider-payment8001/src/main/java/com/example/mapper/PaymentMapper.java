package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entity.Payment;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author booty
 * @since 2021-03-18
 */
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {

}

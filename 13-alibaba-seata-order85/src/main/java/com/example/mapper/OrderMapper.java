package com.example.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entity.Order;
import entity.Payment;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author luminion
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}

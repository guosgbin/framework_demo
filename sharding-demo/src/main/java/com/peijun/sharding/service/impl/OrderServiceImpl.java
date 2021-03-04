package com.peijun.sharding.service.impl;

import com.peijun.sharding.dao.OrderDao;
import com.peijun.sharding.pojo.Order;
import com.peijun.sharding.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author Kwok Dylan GSGB
 * @since 2021-03-04
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService {

}

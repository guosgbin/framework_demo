package com.peijun.transaction.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peijun.transaction.dao.OrderDao;
import com.peijun.transaction.pojo.Order;
import com.peijun.transaction.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

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

    // ----------------- 测试事务失效 开始----------------

    /**
     * 测试事务失效
     * 1.方法不是public修饰
     * 2.MYSQL不是InnoDB引擎
     * 3.normalTest调用A方法，normalTest无事务注解，A有事务注解，这种也不回滚
     * 4.自己try catch异常了
     * 5.当前的类未被注入到容器中
     * 6.@Transactional默认抓取RuntimeException和Error
     */
    @Override
    public void transactionInvalidTest01() {
        A();
    }

    /**
     * A有事务注解 而normalTest没有 会导致事务失效
     */
    @Transactional
    public void A() {
        saveOrder();
        throw new RuntimeException("我抛异常");
    }

    /**
     * 测试事务失效 捕获异常没有抛出
     */
    @Override
    @Transactional
    public void transactionInvalidTest02() {
        try {
            saveOrder();
            throw new RuntimeException("我抛异常");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------- 测试事务失效 结束----------------


    private void saveOrder() {
        Order order = new Order();
        order.setOrderId(100L);
        order.setUserId(10100666);
        order.setPrice(new BigDecimal(new Random().nextInt(100) + 1));
        order.setStatus(1);
        order.setCreateTime(LocalDateTime.now());
        this.save(order);
    }

    // ----------------- 测试事务传播行为  开始----------------
}
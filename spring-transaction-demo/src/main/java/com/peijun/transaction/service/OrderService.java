package com.peijun.transaction.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.peijun.transaction.pojo.Order;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author Kwok Dylan GSGB
 * @since 2021-03-04
 */
public interface OrderService extends IService<Order> {

    /**
     * 测试事务失效 A方法调B方法 A无注解 B有注解
     */
    void transactionInvalidTest01();


    /**
     * 测试事务失效 捕获异常没有抛出
     */
    void transactionInvalidTest02();
}

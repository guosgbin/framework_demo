package com.peijun.sharding.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.peijun.sharding.pojo.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author Kwok Dylan GSGB
 * @since 2021-03-02
 */
public interface OrderDao extends BaseMapper<Order> {

    @Insert("INSERT INTO t_order (order_id, price, user_id, status, create_time)" +
            " VALUES(#{orderId}, #{price}, #{userId}, #{status}, #{createTime})")
    int insertOrder(Order order);

    @Select("SELECT * FROM t_order")
    List<Order> queryAllOrder();

    @Select("SELECT SUM(price) FROM t_order")
    int querySum();

    @Select("SELECT SUM(price) FROM t_order WHERE create_time > '2021-03-01 00:00:00' AND create_time < '2021-03-05 00:00:00'")
    int queryRangeSum();

    @Select("SELECT * FROM t_order")
    IPage<Order> queryPage(IPage<Order> page);

    @Select("SELECT * FROM t_order WHERE create_time = '2021-03-01 00:00:00' ")
    List<Order> queryByCreateTime();

    @Select("SELECT * FROM t_order WHERE create_time > '2021-03-01 00:00:00' AND create_time < '2021-03-05 00:00:00'")
    List<Order> queryRangeByCreateTime();
}

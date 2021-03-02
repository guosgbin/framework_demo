package com.peijun.sharding;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peijun.sharding.dao.OrderDao;
import com.peijun.sharding.pojo.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 测试以 时间 分片
 * 水平分表
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingDemoApplication.class)
public class ShardingCreateTimeTest {

    @Autowired(required = false)
    private OrderDao orderDao;

    /**
     * 测试插入
     * 测试目标：按照月份插入多张表
     */
    @Test
    public void testInsert() {
        String nowStr = "2021-02-22 00:00:00";
        LocalDateTime now = LocalDateTime.parse(nowStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        for (long i = 1; i < 50; i++) {
            Order order = new Order();
            order.setOrderId(i);
            order.setUserId(12345);
            order.setPrice(new BigDecimal(100));
            order.setStatus(1);
            order.setCreateTime(now.plusDays(i)); // 需要设置时间，否则sharding不知道往哪里路由
//            orderDao.insert(order);
            orderDao.insertOrder(order);
        }
    }

    /**
     * 测试查询所有
     */
    @Test
    public void testQueryAll() {
//        List<Order> orders = orderDao.queryAllOrder();

        List<Order> orders = orderDao.selectList(null);
        orders.forEach(System.out::println);
    }

    @Test
    public void testQuerySum() {
        int sum = orderDao.querySum();
        System.out.println(sum);
    }

    @Test
    public void testQueryRangeSum() {
        int sum = orderDao.queryRangeSum();
        System.out.println(sum);
    }

    /**
     * 测试分页
     */
    @Test
    public void testPage() {
        IPage<Order> page = new Page<>(1, 20);
        IPage<Order> page1 = orderDao.queryPage(page);
        for (Order record : page1.getRecords()) {
            System.out.println(record);
        }
     }

    /**
     * 测试 路由
     */
    @Test
    public void testQueryByCreateTime() {
        List<Order> orders = orderDao.queryByCreateTime();
        orders.forEach(System.out::println);
    }

    /**
     * 测试 路由
     */
    @Test
    public void testQueryRangeByCreateTime() {
        List<Order> orders = orderDao.queryRangeByCreateTime();
        orders.forEach(System.out::println);
    }

}

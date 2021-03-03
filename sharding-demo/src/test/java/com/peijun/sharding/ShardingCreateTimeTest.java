package com.peijun.sharding;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
 * <p>
 * 测试插入 {@link #testInsert()}
 * 测试无条件查询所有 {@link #testQueryAll()}
 * 测试无条件分页 {@link #testPage()}
 * 测试条件查询 = {@link #testQueryEq()}
 * 测试范围查询 包含开始结束 {@link #testQueryClosed()}
 * 测试范围查询 不含开始,包含结束 {@link #testQueryNoLower()}
 *
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingDemoApplication.class)
public class ShardingCreateTimeTest {
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired(required = false)
    private OrderDao orderDao;

    /**
     * 测试插入
     * 测试目标：按照月份插入多张表
     */
    @Test
    public void testInsert() {
        String nowStr = "2021-02-22 00:00:00";
        LocalDateTime now = LocalDateTime.parse(nowStr, FORMATTER);
        for (long i = 1; i <= 50; i++) {
            Order order = new Order();
            order.setOrderId(i);
            order.setUserId(12345);
            order.setPrice(new BigDecimal(100));
            order.setStatus(1);
            // 需要设置时间，否则sharding不知道往哪里路由 这样会导致全局路由
            order.setCreateTime(now.plusDays(i));
            orderDao.insert(order);
        }
    }

    /**
     * 测试无条件查询所有
     * 测试目标：路由到所有配置的表
     * actual-data-nodes: m1.t_order_$->{['2021']}_$->{['02', '03','04']} # 数据节点 配置
     */
    @Test
    public void testQueryAll() {
        List<Order> orders = orderDao.selectList(null);
        orders.forEach(System.out::println);
    }

    /**
     * 测试无条件分页
     */
    @Test
    public void testPage() {
        IPage<Order> page = new Page<>(1, 20);
        IPage<Order> orderIPage = orderDao.selectPage(page, null);
        orderIPage.getRecords().forEach(System.out::println);
    }

    /**
     * 测试条件查询 =
     */
    @Test
    public void testQueryEq() {
        LocalDateTime now = LocalDateTime.parse("2021-03-25 00:00:00", FORMATTER);
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Order::getCreateTime, now);
        List<Order> orders = orderDao.selectList(queryWrapper);
        orders.forEach(System.out::println);
    }

    /**
     * 测试条件查询 in
     */
    @Test
    public void testQueryIn() {
        LocalDateTime now1 = LocalDateTime.parse("2021-03-25 00:00:00", FORMATTER);
        LocalDateTime now2 = LocalDateTime.parse("2021-02-25 00:00:00", FORMATTER);
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(Order::getCreateTime, now1, now2);
        List<Order> orders = orderDao.selectList(queryWrapper);
        orders.forEach(System.out::println);
    }

    /**
     * 测试范围查询 包含开始结束
     */
    @Test
    public void testQueryClosed() {
        LocalDateTime startTime = LocalDateTime.parse("2021-01-25 00:00:00", FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse("2021-05-25 00:00:00", FORMATTER);
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.gt(Order::getCreateTime, startTime)
                .lt(Order::getCreateTime, endTime);
        List<Order> orders = orderDao.selectList(queryWrapper);
        orders.forEach(System.out::println);
    }

    /**
     * 测试范围查询 不含开始,包含结束
     */
    @Test
    public void testQueryNoLower() {
        LocalDateTime endTime = LocalDateTime.parse("2021-08-25 00:00:00", FORMATTER);
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.lt(Order::getCreateTime, endTime);
        List<Order> orders = orderDao.selectList(queryWrapper);
        orders.forEach(System.out::println);
    }






//    @Test
//    public void testQuerySum() {
//        int sum = orderDao.querySum();
//        System.out.println(sum);
//    }
//
//    @Test
//    public void testQueryRangeSum() {
//        int sum = orderDao.queryRangeSum();
//        System.out.println(sum);
//    }
//
//
//    /**
//     * 测试 路由
//     */
//    @Test
//    public void testQueryByCreateTime() {
//        List<Order> orders = orderDao.queryByCreateTime();
//        orders.forEach(System.out::println);
//    }
//
//    /**
//     * 测试 路由
//     */
//    @Test
//    public void testQueryRangeByCreateTime() {
//        List<Order> orders = orderDao.queryRangeByCreateTime();
//        orders.forEach(System.out::println);
//    }

}

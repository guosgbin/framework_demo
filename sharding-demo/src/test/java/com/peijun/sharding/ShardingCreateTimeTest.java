package com.peijun.sharding;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.peijun.sharding.dao.OrderDao;
import com.peijun.sharding.dto.OrderDTO;
import com.peijun.sharding.pojo.Order;
import com.peijun.sharding.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 测试以 时间 分片  sharding-jdbc测试版本 4.1.1
 * 水平分表
 * <p>
 * ===== 单表无条件查询 =====
 * 测试插入 {@link #testInsert()}
 * 测试无条件查询所有 {@link #testQueryAll()}
 * 测试无条件分页 {@link #testPage()}
 * <p>
 * ===== 单表条件查询 =====
 * 测试条件查询 = {@link #testQueryEq()}
 * 测试范围查询 包含开始结束 {@link #testQueryClosed()}
 * 测试范围查询 不含开始,包含结束 {@link #testQueryNoLower()}
 * 测试范围查询 包含开始,不含结束 {@link #testQueryNoUpper()}
 * 测试范围查询 BETWEEN AND {@linkplain #testQueryBetweenAnd()}
 * <p>
 * ===== 单表分组 排序等 =====
 * 测试分组 {@link #testQueryGroupBy()}
 * 测试排序 {@link #testQueryDescAndAsc()}
 * <p>
 * ===== 单表聚合查询 =====
 * 测试SUM {@link #testQuerySum()}
 * 测试AVG {@link #testQueryClosed()} 注意SQL改写 补列
 *
 * ===== 联表 =====
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingDemoApplication.class)
public class ShardingCreateTimeTest {
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired(required = false)
    private OrderDao orderDao;
    @Autowired
    private OrderService orderService;

    /* 单表无条件查询 ----------------------------- */

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
            long userId = 10000 + i + i % 2;
            order.setUserId((int) userId);
            order.setPrice(new BigDecimal(new Random().nextInt(100) + 1));
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

    /* 单表条件查询 ----------------------------- */

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
        LocalDateTime endTime = LocalDateTime.parse("2021-05-25 00:00:00", FORMATTER);
//        LocalDateTime endTime = LocalDateTime.parse("2021-08-25 00:00:00", FORMATTER);
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.lt(Order::getCreateTime, endTime);
        List<Order> orders = orderDao.selectList(queryWrapper);
        orders.forEach(System.out::println);
    }

    /**
     * 测试范围查询 不含开始,包含结束
     */
    @Test
    public void testQueryNoUpper() {
        LocalDateTime startTime = LocalDateTime.parse("2021-03-25 00:00:00", FORMATTER);
//        LocalDateTime startTime = LocalDateTime.parse("2021-05-25 00:00:00", FORMATTER);
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.gt(Order::getCreateTime, startTime);
        List<Order> orders = orderDao.selectList(queryWrapper);
        orders.forEach(System.out::println);
    }

    /**
     * BETWEEN AND
     */
    @Test
    public void testQueryBetweenAnd() {
        LocalDateTime startTime = LocalDateTime.parse("2021-03-25 00:00:00", FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse("2021-05-25 00:00:00", FORMATTER);
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.between(Order::getCreateTime, startTime, endTime);
        List<Order> orders = orderDao.selectList(queryWrapper);
        orders.forEach(System.out::println);
    }

    /* 单表聚合查询 ----------------------------- */

    /**
     * 聚合求和
     * 注意不要在聚合函数外面再加函数了 否则会报错的
     * eg.
     * SUM(price) √
     * IFNULL(SUM(price),0) ×
     * ABS(SUM(price)) ×
     * ......
     */
    @Test
    public void testQuerySum() {
        LocalDateTime startTime = LocalDateTime.parse("2021-03-25 00:00:00", FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse("2021-05-25 00:00:00", FORMATTER);

//        BigDecimal sum = orderDao.selectSumNoCondition(); // 无条件聚合
//        BigDecimal sum = orderDao.selectSumNoUpper(startTime); // 条件聚合
//        BigDecimal sum = orderDao.selectSumNoLower(endTime);
        BigDecimal sum = orderDao.selectSumClosed(startTime, endTime);

        System.out.println(sum);
    }

    /**
     * 聚合求和
     * 注意不要在聚合函数外面再加函数了 否则会报错的
     * eg.
     * SUM(price) √
     * IFNULL(SUM(price),0) ×
     * ABS(SUM(price)) ×
     * ......
     */
    @Test
    public void testQueryAvg() {
        LocalDateTime startTime = LocalDateTime.parse("2021-03-25 00:00:00", FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse("2021-05-25 00:00:00", FORMATTER);

//        BigDecimal avg = orderDao.selectAvgNoCondition();
        BigDecimal avg = orderDao.selectAvgClosed(startTime, endTime); // SQL改写 补列

        System.out.println(avg);
    }


    /**
     * 测试GROUP BY 注意SQL改写
     */
    @Test
    public void testQueryGroupBy() {
        LocalDateTime startTime = LocalDateTime.parse("2021-03-25 00:00:00", FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse("2021-05-25 00:00:00", FORMATTER);

//        List<Map> mapList = orderDao.selectGroupByNoCondition(); // SQL改写 补列
        List<Map> mapList = orderDao.selectGroupBy(startTime, endTime); // SQL改写 补列

//        System.out.println(avg);
        mapList.forEach(map -> map.forEach((key, value) ->
                System.out.println(key + "==" + value))
        );
    }

    /**
     * 测试排序 注意SQL改写
     */
    @Test
    public void testQueryDescAndAsc() {
        LocalDateTime startTime = LocalDateTime.parse("2021-03-25 00:00:00", FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse("2021-05-25 00:00:00", FORMATTER);

        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
//        queryWrapper.orderByAsc(Order::getPrice); // 按金额升序
//        queryWrapper.orderByDesc(Order::getPrice); // 按金额降序
//        queryWrapper.orderByDesc(Order::getPrice, Order::getUserId); // 按金额降序后， 按userId降序
//        queryWrapper.orderByAsc(Order::getPrice, Order::getUserId); // 按金额升序后， 按userId升序
        queryWrapper.orderByAsc(Order::getPrice).orderByDesc(Order::getUserId);

        List<Order> orders = orderDao.selectList(queryWrapper);
        orders.forEach(System.out::println);
    }

    /**
     * 测试联表
     */
    @Test
    public void testQueryJoin() {
        LocalDateTime startTime = LocalDateTime.parse("2021-03-25 00:00:00", FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse("2021-05-25 00:00:00", FORMATTER);

//        List<OrderDTO> orderList = orderDao.selectJoinUser();
        List<OrderDTO> orderList = orderDao.selectJoinUserByCondition(startTime, endTime);
        orderList.forEach(System.out::println);
    }

}

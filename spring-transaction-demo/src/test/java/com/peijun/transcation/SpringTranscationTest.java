package com.peijun.transcation;

import com.peijun.transaction.TransactionApplication;
import com.peijun.transaction.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TransactionApplication.class)
@RunWith(SpringRunner.class)
public class SpringTranscationTest {

    @Autowired
    private OrderService orderService;

    /**
     * 测试事务失效 A方法调B方法 A无注解 B有注解
     */
    @Test
    public void testTransactionInvalid01() {
        orderService.transactionInvalidTest01();
    }

    /**
     * 测试事务失效 捕获异常没有抛出
     */
    @Test
    public void testTransactionInvalid02() {
        orderService.transactionInvalidTest02();
    }
}

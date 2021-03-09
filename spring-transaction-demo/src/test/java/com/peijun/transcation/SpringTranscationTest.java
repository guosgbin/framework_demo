package com.peijun.transcation;

import com.peijun.transaction.TransactionApplication;
import com.peijun.transaction.pojo.User1;
import com.peijun.transaction.pojo.User2;
import com.peijun.transaction.service.User1Service;
import com.peijun.transaction.service.User2Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TransactionApplication.class)
@RunWith(SpringRunner.class)
public class SpringTranscationTest {

//    @Autowired
//    private OrderService orderService;
//
//    /**
//     * 测试事务失效 A方法调B方法 A无注解 B有注解
//     */
//    @Test
//    public void testTransactionInvalid01() {
//        orderService.transactionInvalidTest01();
//    }
//
//    /**
//     * 测试事务失效 捕获异常没有抛出
//     */
//    @Test
//    public void testTransactionInvalid02() {
//        orderService.transactionInvalidTest02();
//    }


    @Autowired
    private User1Service user1Service;
    @Autowired
    private User2Service user2Service;

    /**
     * “张三”、“李四”均插入。
     *  外围方法未开启事务，插入“张三”、“李四”方法在自己的事务中独立运行，外围方法异常不影响内部插入“张三”、“李四”方法独立的事务。
     */
    @Test
    public void notransaction_exception_required_required(){
        User1 user1=new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2=new User2();
        user2.setName("李四");
        user2Service.addRequired(user2);

        throw new RuntimeException();
    }

    @Test
    public void notransaction_required_required_exception(){
        User1 user1=new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2=new User2();
        user2.setName("李四");
        user2Service.addRequiredException(user2);
    }


}

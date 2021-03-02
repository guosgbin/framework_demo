package com.peijun.rocketmq.filter;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * RocketMQ只定义了一些基本语法来支持这个特性。你也可以很容易地扩展它。
 *
 * 数值比较，比如：>，>=，<，<=，BETWEEN，=；
 * 字符比较，比如：=，<>，IN；
 * IS NULL 或者 IS NOT NULL；
 * 逻辑符号 AND，OR，NOT；
 * 常量支持类型为：
 *
 * 数值，比如：123，3.1415；
 * 字符，比如：'abc'，必须用单引号包裹起来；
 * NULL，特殊的常量
 * 布尔值，TRUE 或 FALSE
 *
 *
 * 只有使用push模式的消费者才能用使用SQL92标准的sql语句，接口如下：
 * public void subscribe(finalString topic, final MessageSelector messageSelector)
 */
public class SqlFilterConsumer {

    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("sql_filter_consumer_group");
        consumer.setNamesrvAddr("8.129.224.250:9876");
        // TODO 已经设置了 不知为何不行 下次再试
        // Don't forget to set enablePropertyFilter=true in broker
        consumer.subscribe("SqlFilterTest",
            // 接收  TagA 和 TagB 且 a 在0和3之间
            MessageSelector.bySql("(TAGS is not null and TAGS in ('TagA', 'TagB'))" +
                "and (a is not null and a between 0 and 3)"));

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}
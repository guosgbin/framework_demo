package com.peijun.rocketmq.quickstart;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 快速入门  如何订阅和消费消息 {@link DefaultMQPushConsumer}
 */
public class Consumer {
    public static void main(String[] args) throws InterruptedException, MQClientException {
        // 指定组名实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("quick_start_group");
        // 声明name server的地址
        // consumer.setNamesrvAddr("name-server1-ip:9876;name-server2-ip:9876");
        consumer.setNamesrvAddr("8.129.224.110:9876");

        // Specify where to start in case the specified consumer group is a brand new one.
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 订阅主题
        consumer.subscribe("TopicTest", "*");
        // 消息到达brokers时 执行注册回调
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 开启消费者
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}
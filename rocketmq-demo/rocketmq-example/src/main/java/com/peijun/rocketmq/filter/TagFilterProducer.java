package com.peijun.rocketmq.filter;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 在大多数情况下，TAG是一个简单而有用的设计，其可以来选择您想要的消息。例如：
 *
 * DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CID_EXAMPLE");
 * consumer.subscribe("TOPIC", "TAGA || TAGB || TAGC");
 *
 * 消费者将接收包含TAGA或TAGB或TAGC的消息。但是限制是一个消息只能有一个标签，这对于复杂的场景可能不起作用。
 * 在这种情况下，可以使用SQL表达式筛选消息。SQL特性可以通过发送消息时的属性来进行计算。
 * 在RocketMQ定义的语法下，可以实现一些简单的逻辑。下面是一个例子：
 *
 * ------------
 * | message  |
 * |----------|  a > 5 AND b = 'abc'
 * | a = 10   |  --------------------> Gotten
 * | b = 'abc'|
 * | c = true |
 * ------------
 * ------------
 * | message  |
 * |----------|   a > 5 AND b = 'abc'
 * | a = 1    |  --------------------> Missed
 * | b = 'abc'|
 * | c = true |
 * ------------
 */
public class TagFilterProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("tag_filter_producer_group");
        producer.setNamesrvAddr("8.129.224.250:9876");
        producer.start();
        String[] tags = new String[] {"TagA", "TagB", "TagC"};
        // 循环发送消息
        for (int i = 0; i < 10; i++) {
            Message msg = new Message("TagFilterTest",
                tags[i % tags.length],
                "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg); // 发送同步消息
            System.out.printf("%s%n", sendResult);
        }
        producer.shutdown();
    }
}
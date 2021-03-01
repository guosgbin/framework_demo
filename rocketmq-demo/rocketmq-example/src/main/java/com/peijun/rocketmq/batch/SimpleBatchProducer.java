package com.peijun.rocketmq.batch;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量发送消息
 *
 * 如果您每次只发送不超过4MB的消息，则很容易使用批处理
 * 文档上面说的是4MB， 而代码里面的注释是1MiB
 */
public class SimpleBatchProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("batch_producer_group");
        producer.setNamesrvAddr("8.129.224.250:9876");
        producer.start();

        // If you just send messages of no more than 1MiB at a time, it is easy to use batch
        // Messages of the same batch should have: same topic, same waitStoreMsgOK and no schedule support
        String topic = "BatchTest";
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(topic, "TagF", "OrderID001", "Hello world 0".getBytes()));
        messages.add(new Message(topic, "TagF", "OrderID002", "Hello world 1".getBytes()));
        messages.add(new Message(topic, "TagF", "OrderID003", "Hello world 2".getBytes()));
        SendResult send = producer.send(messages);
        System.out.println(send.getMsgId() + "=====" + send.getSendStatus());
    }
}
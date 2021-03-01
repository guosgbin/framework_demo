package com.peijun.rocketmq.quickstart;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * 快速入门 发送消息 {@link DefaultMQProducer}
 *
 * RocketMQ - 阿里云部署、及外网访问的那些个问题
 * https://my.oschina.net/xiaowangqiongyou/blog/1823400
 *
 * 开启broker
 * nohup sh  ./bin/mqbroker -n 8.129.224.250:9876  -c ./conf/broker.conf autoCreateTopicEnable=true  > mqbroker.log &
 *
 * cip.cc
 * ifconfig.me
 *
 *
 * $ sh /usr/local/rocketmq/bin/mqadmin consumerProgress -n 127.0.0.1:9876  | awk '{print $1,$7}'              //所有消费组group
 * $ sh /usr/local/rocketmq/bin/mqadmin topicList -n 127.0.0.1:9876                                   //查看所有topic
 * $ sh /usr/local/rocketmq/bin/mqadmin topicstatus -n 127.0.0.1:9876 -t contract_match_market_push_deeps                //查看Topic状态
 * $ sh /usr/local/rocketmq/bin/mqadmin  clusterList -n 127.0.0.1:9876                                //查询集群消息
 * $ mqadmin consumerProgress -g <ConsumerGroup> -n <NameServerAddr>                                                     //查看Consumer Group订阅了哪些TOPIC
 * $ sh /usr/local/rocketmq/bin/mqadmin statsAll -n localhost:9876 | grep  usdt-marked-price                             //查看主题的输入输出
 *
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        // 创建生产者实例 指定组名
        DefaultMQProducer producer = new DefaultMQProducer("quick_start_group");
        // 声明name server的地址
        producer.setNamesrvAddr("8.129.224.110:9876");
//        producer.setSendMsgTimeout(6000);

        producer.setVipChannelEnabled(false);
        // 开启生产者
        producer.start();
        // 发送消息
        for (int i = 0; i < 10; i++) {
            try {
                // 创建一个消息 指定 topic tag和消息主题
                Message msg = new Message("TopicTest" , // 主题
                    "TagA" , // tag
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)  // 消息主体
                );
                // 发送消息到一个broker
                SendResult sendResult = producer.send(msg);
                System.out.printf("%s%n", sendResult);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }
        // 不再使用producer就关闭producer
        producer.shutdown();
    }
}
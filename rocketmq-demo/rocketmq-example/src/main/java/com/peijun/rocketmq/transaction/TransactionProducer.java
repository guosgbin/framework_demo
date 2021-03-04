package com.peijun.rocketmq.transaction;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

/**
 * 事务消息
 * <p>
 * 事务消息共有三种状态，提交状态、回滚状态、中间状态：
 * <p>
 * TransactionStatus.CommitTransaction: 提交事务，它允许消费者消费此消息。
 * TransactionStatus.RollbackTransaction: 回滚事务，它代表该消息将被删除，不允许被消费。
 * TransactionStatus.Unknown: 中间状态，它代表需要检查消息队列来确定状态。
 * <p>
 * 使用 TransactionMQProducer类创建生产者，并指定唯一的 ProducerGroup，
 * 就可以设置自定义线程池来处理这些检查请求。
 * 执行本地事务后、需要根据执行结果对消息队列进行回复。回传的事务状态在请参考前一节。
 *
 * 当发送半消息成功时，我们使用 executeLocalTransaction 方法来执行本地事务。
 * 它返回前一节中提到的三个事务状态之一。
 * checkLocalTransaction 方法用于检查本地事务状态，并回应消息队列的检查请求。它也是返回前一节中提到的三个事务状态之一。
 *
 *
 * 事务消息不支持延时消息和批量消息。
 *
 * 为了避免单个消息被检查太多次而导致半队列消息累积，我们默认将单个消息的检查次数限制为 15 次，
 * 但是用户可以通过 Broker 配置文件的 transactionCheckMax参数来修改此限制。
 * 如果已经检查某条消息超过 N 次的话（ N = transactionCheckMax ） 则 Broker 将丢弃此消息，并在默认情况下同时打印错误日志。
 * 用户可以通过重写 AbstractTransactionalMessageCheckListener 类来修改这个行为。
 *
 * 事务消息将在 Broker 配置文件中的参数 transactionTimeout 这样的特定时间长度之后被检查。
 * 当发送事务消息时，用户还可以通过设置用户属性 CHECK_IMMUNITY_TIME_IN_SECONDS 来改变这个限制，该参数优先于 transactionTimeout 参数。
 *
 * 事务性消息可能不止一次被检查或消费。
 * 提交给用户的目标主题消息可能会失败，目前这依日志的记录而定。
 * 它的高可用性通过 RocketMQ 本身的高可用性机制来保证，如果希望确保事务消息不丢失、并且事务完整性得到保证，建议使用同步的双重写入机制。
 * 事务消息的生产者 ID 不能与其他类型消息的生产者 ID 共享。
 * 与其他类型的消息不同，事务消息允许反向查询、MQ服务器能通过它们的生产者 ID 查询到消费者。
 */
public class TransactionProducer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        TransactionListener transactionListener = new TransactionListenerImpl();
        // 创建事务生产者
        TransactionMQProducer producer = new TransactionMQProducer("transaction_producer_group");
        // 创建一个线程池，自定义线程工厂，为了自定义线程名称
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        // 设置线程池
        producer.setExecutorService(executorService);
        // 设置事务监听者
        producer.setTransactionListener(transactionListener);
        producer.start();
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
        // 发送消息
        for (int i = 0; i < 10; i++) {
            try {
                Message msg =
                        new Message("TopicTest1234", tags[i % tags.length], "KEY" + i,
                                ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.sendMessageInTransaction(msg, null);
                System.out.printf("%s%n", sendResult);
                Thread.sleep(10);
            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
        }
        producer.shutdown();
    }
}
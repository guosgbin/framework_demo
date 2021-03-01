package com.peijun.rocketmq.batch;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 复杂度只有当你发送大批量时才会增长，你可能不确定它是否超过了大小限制（4MB）。
 * 这时候你最好把你的消息列表分割一下
 */
public class SplitBatchProducer {

    public static void main(String[] args) throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("batch_producer_group");
        producer.setNamesrvAddr("8.129.224.250:9876");
        producer.start();

        // large batch
        String topic = "BatchTest";
        List<Message> messages = new ArrayList<>(100 * 1000);
        for (int i = 0; i < 100 * 1000; i++) {
            messages.add(new Message(topic, "TagF", "OrderID" + i, ("Hello world " + i).getBytes()));
        }

        // 把大的消息分裂成若干个小的消息
        ListSplitter splitter = new ListSplitter(messages);
        while (splitter.hasNext()) {
            try {
                List<Message> listItem = splitter.next();
                producer.send(listItem);
            } catch (Exception e) {
                e.printStackTrace();
                // 处理error
            }
        }
    }

    /**
     * 分割类 其实guvua里有工具类
     */
    static class ListSplitter implements Iterator<List<Message>> {
        // 分割单个的 容量限制
        private int sizeLimit = 1000 * 1000;
        // 消息集合
        private final List<Message> messages;
        // 指向当前元素位置的指针
        private int currIndex;

        public ListSplitter(List<Message> messages) {
            this.messages = messages;
        }

        @Override
        public boolean hasNext() {
            return currIndex < messages.size();
        }

        /**
         * 获取方法
         */
        @Override
        public List<Message> next() {
            int nextIndex = currIndex;
            int totalSize = 0;
            for (; nextIndex < messages.size(); nextIndex++) {
                Message message = messages.get(nextIndex);
                // 获取单个消息的长度 开始...
                int tmpSize = message.getTopic().length() + message.getBody().length;
                Map<String, String> properties = message.getProperties();
                for (Map.Entry<String, String> entry : properties.entrySet()) {
                    tmpSize += entry.getKey().length() + entry.getValue().length();
                }
                tmpSize = tmpSize + 20; // for log overhead 为了日志头
                // 获取单个消息的长度 结束...
                if (tmpSize > sizeLimit) {
                    // it is unexpected that single message exceeds the sizeLimit
                    // here just let it go, otherwise it will block the splitting process
                    // 到这里面表示 单个消息超过了sizeLimit限制 结束循环 否则锁住分割进程
                    if (nextIndex - currIndex == 0) {
                        // if the next sublist has no element, add this one and then break, otherwise just break
                        // 集合没有元素了，自增一下
                        nextIndex++;
                    }
                    break;
                }
                if (tmpSize + totalSize > sizeLimit) {
                    break;
                } else {
                    totalSize += tmpSize;
                }

            }
            List<Message> subList = messages.subList(currIndex, nextIndex);
            currIndex = nextIndex;
            return subList;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not allowed to remove");
        }
    }

}

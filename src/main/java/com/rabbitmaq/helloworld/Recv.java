package com.rabbitmaq.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author wang
 * @description
 * 消费者监听来自RabbitMQ的消息，所以不像发布者只发布一条消息，
 * 我们会让消费者一直运行来监听消息并将其打印出来。
 * @create 2020/12/17 0:12
 */

public class Recv {
    private final static String QUEUE_NAME = "hello";
    public static void main(String[] argv) throws Exception {
        //发布者相同;我们打开一个连接和一个通道，并声明我们将从中消费的队列。注意，这与发送publish到的队列匹配（队列名hello）。
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.99.68.32");
        //为什么我们不使用try-with-resource语句来自动关闭通道和连接？
        //这样做，我们可以简单地让程序继续，关闭所有内容，然后退出!
        //但是这样做很笨拙，因为我们希望在使用者异步侦听消息到达时 ，进程保持活动状态。
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //请注意，我们在这里也声明了队列。
        // 因为我们可能会在发布服务器之前启动消费者，所以我们希望在尝试使用来自队列的消息之前确保队列已经存在。
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        //我们将告诉服务器从队列中传递消息。
        // 因为它将异步推送消息给我们，所以我们提供一个对象形式的回调，它将缓冲消息，直到我们准备好使用它们。
        // 这就是DeliverCallback的子类所做的事情。

        DeliverCallback deliverCallback = new DeliverCallback() {
            //接口不能实例化，接口的实现类才可以。new接口其实new的是接口的实现类，所以必须写上相关方法。
            @Override
            public void handle(String consumerTag, Delivery delivery) throws IOException {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };

         /* java 8 的lambda表达式
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        */

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}

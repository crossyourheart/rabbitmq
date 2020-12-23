package com.rabbitmaq.publishsubscribe;

/**
 * @author wang
 * @description
 * @create 2020/12/23 22:10
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogs {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.99.68.32");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明交换机，因为不知道生产者和消费者哪个先启动，都一起声明。
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //创建临时队列，返回随机的队列名称
        String queueName = channel.queueDeclare().getQueue();
        //绑定交换机和队列，fanout类型的交换机 routingKey会被忽略
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}

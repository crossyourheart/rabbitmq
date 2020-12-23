package com.rabbitmaq.publishsubscribe;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 生产者程序发出日志消息，看起来与前一教程没有太大区别。
 * 最重要的变化是，我们现在希望向logs日志交换机发布消息，而不是向无名的日志交换机发布消息。
 * 我们需要在发送时提供一个routingKey，但是它的值在扇形交换机中被忽略。
 * @author wang
 * @description
 * @create 2020/12/23 22:05
 */
public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.99.68.32");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            //建立连接之后，声明了一个fanout类型交换机 logs
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            String message = argv.length < 1 ? "info: Hello World!" :
                    String.join(" ", argv);
            //如果还没有队列绑定到exchange，消息就会丢失，但这对我们来说没问题;
            //如果没有消费者正在收听，我们可以安全地丢弃消息。
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}

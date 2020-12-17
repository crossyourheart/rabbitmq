package com.rabbitmaq.helloworld;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
/**
 * @author wang
 * @description
 * @create 2020/12/16 23:57
 */

public class Send {
    private final static String QUEUE_NAME = "hello";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.99.68.32");
        //create a connection to the server
        try (//该连接抽象了套接字连接，并为我们处理协议版本协商和身份验证等。
             Connection connection = factory.newConnection();
             //接下来，我们创建一个通道，用于完成工作的大多数API驻留在此。
             //注意，我们可以使用带有资源的try-with-resources语句，因为连接和通道都实现了java.io.Closeable。这样我们就不需要在代码中显式地关闭它们。
             Channel channel = connection.createChannel()) {
            //要发送，我们必须声明一个队列供我们发送到;然后我们可以在try-with-resources语句中向队列发布消息
            //声明一个队列是幂等的——它只会在队列不存在的情况下被创建。消息内容是一个字节数组，因此您可以在这里对任何内容进行编码。
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!!11!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}

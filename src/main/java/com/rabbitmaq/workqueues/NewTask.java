package com.rabbitmaq.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @author wang
 * @description
 * 我们将稍微修改前面示例中的Send.java代码，以允许从命令行发送任意消息。
 * 这个程序将把任务添加到我们的工作队列中，所以我们将它命名为NewTask.java
 * @create 2020/12/17 23:05
 */
public class NewTask {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.99.68.32");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
            Scanner scanner = new Scanner(System.in);
            String message = null;
            do{
                System.out.println("请输入一个字符串：");
                message = scanner.nextLine();
                channel.basicPublish("", TASK_QUEUE_NAME,
                        MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }while (scanner.hasNextLine());
        }
    }

}

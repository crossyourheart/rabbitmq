package com.rabbitmaq.workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author wang wei
 * @description
 * 我们旧的Recv.java程序也需要做一些更改:它需要为消息体中的每个点假做一秒钟的工作。
 * 它将处理传递的消息并执行任务，因此我们将其称为Worker.java
 * @create 2020/12/17 23:12
 */

public class Worker {

    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.99.68.32");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        channel.basicQos(2);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] 收到 '" + message + "'");
            long l1 = 0,l2 = 0;
            try {
                 l1 = System.currentTimeMillis();
                doWork(message);
                 l2 = System.currentTimeMillis();
            } finally {
                long l = l2 - l1;
                System.out.println("花费"+(l2 - l1)+"s");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        channel.basicConsume(TASK_QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }

    /**
     * 在前一部分中，我们发送了一个包含“Hello World!”的消息。现在我们将发送字符串来代表复杂的任务。
     * 我们没有现实生活中的任务，比如要调整图像大小或要渲染pdf文件，所以让我们假装自己很忙——通过使用Thread.sleep()函数。
     * 我们用字符串中点的数量表示它的复杂度;每一个点将占“工作”的一秒钟。例如，一个由"Hello..."描述的伪任务… 需要三秒钟。
     */
    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                try {
                    //模拟执行时间
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

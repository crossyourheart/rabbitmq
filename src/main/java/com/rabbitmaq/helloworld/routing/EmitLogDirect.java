package com.rabbitmaq.helloworld.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.UUID;

/**
 * @author wang
 * @description
 * @create 2020/12/23 23:00
 */

public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.99.68.32");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            String severity = getSeverity();
            String message = UUID.randomUUID().toString();

            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
        }
    }

    private static String getSeverity() {
        int i = (int) (Math.random() * 3);
        System.out.println(i);
        return String.valueOf(i);
    }

}
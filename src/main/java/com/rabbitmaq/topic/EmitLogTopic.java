package com.rabbitmaq.topic;

/**
 * @author wang
 * @description
 * @create 2020/12/23 23:40
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.UUID;

public class EmitLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.99.68.32");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            String routingKey  = getRoutingKey();
            String message = UUID.randomUUID().toString();

            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
        }
    }
    private static String getRoutingKey() {
        int i = (int) (Math.random() * 3);
        String key = null;
                if (i==0){
                    key = "1.1.1" ;
                }
                else {
                    key = "2.3";
                }
        return key;
    }
}

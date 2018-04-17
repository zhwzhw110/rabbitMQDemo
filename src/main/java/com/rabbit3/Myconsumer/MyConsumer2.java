package com.rabbit3.Myconsumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/17 0017 下午 5:01
 * @DESC :
 */
public class MyConsumer2 {

    private static String exchange_name="publicSend";

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        String queue_name = channel.queueDeclare().getQueue();

        channel.queueBind(queue_name,exchange_name,"");
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("C [x] Received '" + message + "'");
            }
        };
        //自动回复队列应答
        channel.basicConsume(queue_name, true, consumer);
    }
}

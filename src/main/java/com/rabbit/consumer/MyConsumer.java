package com.rabbit.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/17 0017 下午 2:28
 * @DESC : 消息的消费者
 */
public class MyConsumer {
    private static String queue_name = "firstQueue"; //设置队列的名称

    public static void main(String[] args) throws Exception {
        //创建一个工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务器ip
        factory.setHost("localhost");
        //获取一个连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        //声明要关注的队列
        channel.queueDeclare(queue_name, false, false, false, null);
        System.out.println("C [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("C [x] Received '" + message + "'");
            }
        };
        //自动回复队列应答 -- RabbitMQ中的消息确认机制，后面章节会详细讲解
        channel.basicConsume(queue_name, true, consumer);
    }
}
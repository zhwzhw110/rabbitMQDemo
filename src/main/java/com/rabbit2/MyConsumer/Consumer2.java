package com.rabbit2.MyConsumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/17 0017 下午 3:09
 * @DESC :
 */
public class Consumer2 {
    private static String queue_name = "taskfirstQueue2"; //设置队列的名称
    public static void main(String[] args) throws  Exception{
        //创建一个工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务器ip
        factory.setHost("localhost");
        //获取一个连接
        Connection connection = factory.newConnection();
        //创建一个通道
        final Channel channel = connection.createChannel();
        //声明要关注的队列
        channel.queueDeclare(queue_name, false, false, false, null);
        System.out.println("C [*] Waiting for messages. To exit press CTRL+C");
        // 每次从队列中获取数量
        channel.basicQos(1);
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("C [x] Received '" + message + "'");
                    try {
                        doWork(message);
                    }finally {
                        System.out.println("down");
                        //multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息。
                        channel.basicAck(envelope.getDeliveryTag(),false); //手动应答ack
                    }
            }
        };
        //自动回复队列应答
        //false:设置成false 自动应答机制
        // autoAck：是否自动ack，如果不自动ack，需要使用channel.ack、channel.nack、channel.basicReject 进行消息应答
        channel.basicConsume(queue_name, false, consumer);
    }

    private static void doWork(String task) {
        try {
            Thread.sleep(1000); // 暂停1秒钟
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}


package com.rabbit5.MyConsumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/18 0018 上午 10:33
 * @DESC :
 */
public class Consumer {
    private static String exchange_name = "topicExchange";
    private static String exchange_type = "topic";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        String queueName = channel.queueDeclare().getQueue();
        //设置交换器名称和交换类型
        channel.exchangeDeclare(exchange_name,exchange_type);

        // 路由关键字
        String[] routingKeys = new String[]{"*.orange.*"};

        //绑定路由器关键字
        for (int i = 0; i<routingKeys.length;i++){
            channel.queueBind(queueName,exchange_name,routingKeys[i]);
            System.out.println("ReceiveLogsTopic1 exchange:"+routingKeys[i]+", queue:"+queueName+", BindRoutingKey:" + routingKeys[i]);
        }
        //创建接受者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("C [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);


    }

}

package com.rabbit4.MyConsumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/18 0018 上午 9:27
 * @DESC :路由方式 获取消息
 */
public class Consumer {

    private static String exchagnge_name = "exchagngerouting";
    private static String exchagnge_type = "direct";
    private static String[] routingKeys= {"error","info","warning"};

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        //声明交换器
        channel.exchangeDeclare(exchagnge_name,exchagnge_type);
        //获取匿名队列名称
        String queueName = channel.queueDeclare().getQueue();
        // 根据路由关键字进行多重绑定
        for (String routingKey : routingKeys){
            channel.queueBind(queueName,exchagnge_name,routingKey);
            System.out.println("ReceiveLogsDirect1 exchange:"+exchagnge_name+", queue:"+queueName+", BindRoutingKey:" + routingKey);
        }
        //获取
        com.rabbitmq.client.Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("C [x] Received '" + message + "'");
            }
        };
        //自动回复队列应答
        channel.basicConsume(queueName, true, consumer);

    }

}

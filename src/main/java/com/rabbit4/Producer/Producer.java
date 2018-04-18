package com.rabbit4.Producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/18 0018 上午 9:03
 * @DESC : Routing 消息路由 设置
 */
public class Producer {

    private static String exchagnge_type = "direct";
    private static String exchagnge_name = "exchagngerouting";
    private static String[] routingKeys= {"error","info","warning"};

    public static void main(String[] args) throws Exception{
        
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchagnge_name,exchagnge_type);

        for(String routingKey : routingKeys){
            String message = "Send the message level:" + routingKey;
            channel.basicPublish(exchagnge_name,routingKey,null,message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");
        }
        //关闭连接
        channel.close();
        connection.close();

    }

}

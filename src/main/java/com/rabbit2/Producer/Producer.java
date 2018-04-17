package com.rabbit2.Producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/17 0017 下午 2:53
 * @DESC :
 */
public class Producer {

    private static String exchange_type = "fanout"; //设置成广播模式
    private static String exchange_name = "zhw"; //设置exchange的名称
    private static String queue_name = "taskfirstQueue2"; //设置队列的名称

    public static void main(String[] args) throws  Exception{
        //创建一个工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务器ip
        factory.setHost("localhost");
        //获取一个连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        //设置Exchange（exchange名称，Exchange的名字）
        //channel.exchangeDeclare(exchange_name,exchange_type);
        //声明一个队列
        channel.queueDeclare(queue_name, false, false, false, null);

        for(int i = 0; i<10;i++){
            //设置要发送的消息
            String message = "hello world"+i;
            //发送消息到队列中
            channel.basicPublish("", queue_name,  MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println("Producer [x] Sent '" + message + "'");
        }
        //关闭频道和连接
        channel.close();
        connection.close();

    }
}

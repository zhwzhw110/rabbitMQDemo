package com.NIODemo;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/20 0020 上午 11:59
 * @DESC : 通道测试
 */
public class ChannelDemo {

    @Test
    public  void Client(){
        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9899));

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            String msg = "abcdef";
            //数据放入缓冲区
            byteBuffer.put(msg.getBytes());
            //通道中的数据存入缓冲区 writeFrom  readTo
            byteBuffer.flip(); //转化为写模式
            socketChannel.write(byteBuffer); //缓冲区的数据写入到通道中

            //读取
            while(socketChannel.read(byteBuffer)!=-1){
                byteBuffer.flip();
                System.out.println(new String(byteBuffer.array(),0,byteBuffer.limit()));
                byteBuffer.clear();
            }

            socketChannel.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void Server(){
        try {
            //获取通道
            ServerSocketChannel  serverchannel = ServerSocketChannel.open();
            //serverchannel.configureBlocking(false);//设置成非阻塞式的
            //绑定端口号
            serverchannel.bind(new InetSocketAddress("127.0.0.1",9899)); //服务端不需要绑定 IP地址
            //获取客户端连接的
            SocketChannel socketChannel = serverchannel.accept();

            ByteBuffer byteBuf = ByteBuffer.allocate(1024);
            //通道数据到缓冲区
            socketChannel.read(byteBuf);
            byteBuf.flip();
            System.out.println(new String(byteBuf.array(),0,byteBuf.limit()));

            byteBuf.clear();//清空缓冲区

            //数据反馈给客户端 (从缓冲区 写入数据到 通道)
            byteBuf.put("服务器收到信息，处理完了。。".getBytes());
            byteBuf.flip();//转化为写模式
            socketChannel.write(byteBuf);

            socketChannel.close();
            serverchannel.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

package com.NIODemo2;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/23 0023 下午 5:00
 * @DESC :
 */
public class NIOSocketDemo {
    @Test
    public void server() throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9898));
        //设置成非阻塞
        serverSocketChannel.configureBlocking(false);

        //使用选择器
        Selector selector = Selector.open(); //监听“接收事件”
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select()>0){

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false); //设置成非阻塞模式
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }else if(key.isReadable()){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    int len = 0;
                    while ((len=socketChannel.read(byteBuffer))>0){
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(),0,byteBuffer.limit()));
                        byteBuffer.clear();
                    }

                }
            }
            //取消选择键
            iterator.remove();

        }

    }

    @Test
    public void client() throws  Exception{
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));
        //设置成非阻塞模式
        socketChannel.configureBlocking(false);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(new Date().toString().getBytes());

        //转化为写模式
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        byteBuffer.clear();
        socketChannel.close();


    }

}


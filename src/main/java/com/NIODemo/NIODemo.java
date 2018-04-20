package com.NIODemo;

import java.nio.ByteBuffer;

/**
 * @author: zhangHaiWen
 * @date : 2018/4/19 0019 下午 6:21
 * @DESC :
 */
public class NIODemo {

    public static void main(String[] args) {
        ByteBuffer byt = ByteBuffer.allocate(1024);

        System.out.println("position="+byt.position()); //当前游标位置
        System.out.println("limit="+byt.limit());//操作界限
        System.out.println("capacity="+byt.capacity());//一共能储存的空间


        System.out.println("------------------PUT-------------------------");
        String msg = "abcde";
        byt.put(msg.getBytes()); //将数据存入缓冲区
        System.out.println("position="+byt.position());
        System.out.println("limit="+byt.limit());
        System.out.println("capacity="+byt.capacity());

        System.out.println("---------------------flip----------------------");
        byt.flip(); //转化操作 写数据变为读数据
        System.out.println("position="+byt.position());
        System.out.println("limit="+byt.limit());
        System.out.println("capacity="+byt.capacity());



        byte[] bs = new byte[byt.limit()];
        byt.get(bs);
        System.out.println("---------------------get----------------------");
        System.out.println(new String(bs,0,bs.length));
        System.out.println("position="+byt.position());
        System.out.println("limit="+byt.limit());
        System.out.println("capacity="+byt.capacity());

        //重新回到读模式
        byt.rewind();

        //清空缓冲区 数据还是存在的 数据进入被遗忘状态
        byt.clear();
        System.out.println("---------------------clear then put----------------------");
        byte[] newbs =  new byte[3];
        System.out.println( byt.get(newbs));

        System.out.println("position="+byt.position());
        System.out.println("limit="+byt.limit());
        System.out.println("capacity="+byt.capacity());


        System.out.println("capacity="+byt.remaining());


    }

}

package com.me;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UDPServer {

    public static void main(String[] args) throws Exception {
        //因为UDP没有创建连接，数据包也是一次收发一个，所以没有流的概念。
        //Java中使用UDP编程，仍然需要使用Socket，因为应用程序在使用UDP时必须指定网络接口（IP）和端口号。
        // 注意：UDP端口和TCP端口虽然都使用0~65535，但他们是两套独立的端口，即一个应用程序用TCP占用了端口1234，
        // 不影响另一个应用程序用UDP占用端口1234。

        //服务器端，使用UDP也需要监听指定的端口。Java提供了DatagramSocket来实现这个功能
        DatagramSocket ds = new DatagramSocket(6666); // 监听指定端口
        for (;;) { // 无限循环
            // 数据缓冲区:
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            ds.receive(packet); // 收取一个UDP数据包
            // 收取到的数据存储在buffer中，由packet.getOffset(), packet.getLength()指定起始位置和长度
            // 将其按UTF-8编码转换为String:
            String s = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
            System.out.println("from client:"+s);
            //当服务器收到一个DatagramPacket后，通常必须立刻回复一个或多个UDP包，
            // 因为客户端地址在DatagramPacket中，每次收到的DatagramPacket可能是不同的客户端，
            // 如果不回复，客户端就收不到任何UDP包。
            // 发送数据:
            byte[] data = "ACK".getBytes(StandardCharsets.UTF_8);
            packet.setData(data);
            ds.send(packet);
        }

    }

}

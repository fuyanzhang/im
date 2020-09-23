package com.muchen.im.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * ClassName SendTcp
 * Description TODO
 * Author fuyanzhang
 * Date 2020/9/23 15:22
 **/
public class SendTcp implements Runnable {
    private static int BLOCK = 4096;

    @Override
    public void run() {
        try {
            ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);

            ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("localhost", 8144));
            socketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);

            String content = "这是一个帅帅的测试串";
            sendbuffer.clear();
            sendbuffer.put(content.getBytes());
            sendbuffer.flip();
            while (sendbuffer.hasRemaining()) {
                System.out.println("2222");
                socketChannel.write(sendbuffer);
            }

            selector.select(100);
            while (socketChannel.read(receivebuffer) > 0) {
                receivebuffer.flip();
                System.out.println("开始接收数据");
                System.out.println(new String(receivebuffer.array()));
                receivebuffer.clear();
            }

            socketChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

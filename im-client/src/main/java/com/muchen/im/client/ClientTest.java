package com.muchen.im.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * ClassName ClientTest
 * Description TODO
 * Author fuyanzhang
 * Date 2020/9/23 14:50
 **/
public class ClientTest {

    private static int BLOCK = 4096;

    private static ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);

    private static ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);

    public static void main(String[] args) throws IOException, InterruptedException {
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
    }
}

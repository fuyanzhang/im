package com.muchen.im.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * ClassName ClientStarter
 * Description TODO
 * Author fuyanzhang
 * Date 2020/8/25 10:00
 **/
public class ClientStarter {

    private static int BLOCK = 4096;

    private static ByteBuffer sendbuffer = ByteBuffer.allocate(BLOCK);

    private static ByteBuffer receivebuffer = ByteBuffer.allocate(BLOCK);

    public static void main(String[] args) throws IOException, InterruptedException {
        //发送消息
        String content = "这是一个帅帅的测试串";
        //建立连接
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8144));

        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        Thread t = new Thread(new ClientReceiveHandler(selector, socketChannel));
        t.start();

        sendbuffer.clear();
        sendbuffer.put(content.getBytes());
        sendbuffer.flip();
        while (sendbuffer.hasRemaining()) {
            System.out.println("111");
            socketChannel.write(sendbuffer);
        }

        t.join();
//        socketChannel.close();

    }
}

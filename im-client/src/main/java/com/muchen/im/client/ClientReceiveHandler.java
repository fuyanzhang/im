package com.muchen.im.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * ClassName ClientReceiveHandler
 * Description 读取服务端的响应
 * Author fuyanzhang
 * Date 2020/8/27 9:31
 **/
public class ClientReceiveHandler implements Runnable {
    private Selector selector;

    private SocketChannel channel;

    public ClientReceiveHandler(Selector selector, SocketChannel channel) {
        this.channel = channel;
        this.selector = selector;
    }

    @Override
    public void run() {

        try {
            long startTime = System.currentTimeMillis();
            while (selector.select() == 0) {
                System.out.println("1111");
                Thread.sleep(10);
                long currentTime = System.currentTimeMillis();
                if (currentTime - startTime > 30000) {
                    System.out.println("time out");
                    return;
                }
            }
            channel.register(selector, SelectionKey.OP_READ);
            int readChannel = selector.select();
            if (readChannel == 0) {
                return;
            }
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey sk = iter.next();
                if (sk.isReadable()) {
                    handleRead(sk);
                }
                iter.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRead(SelectionKey sk) throws IOException {
        SocketChannel channel = (SocketChannel) sk.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        int i = channel.read(buffer);
        if (i != -1) {
            String msg = new String(buffer.array()).trim();
            System.out.println("server received message: " + msg);
            System.out.println("server reply: " + msg);
//            channel.register(selector, SelectionKey.OP_READ);
            channel.write(ByteBuffer.wrap(msg.getBytes()));
        }
    }
}

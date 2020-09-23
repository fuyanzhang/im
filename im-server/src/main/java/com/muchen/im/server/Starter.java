package com.muchen.im.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * ClassName Starter
 * Description TODO
 * Author fuyanzhang
 * Date 2020/8/24 20:38
 **/
public class Starter {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress(8144), 100);
        channel.configureBlocking(false);
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int readChannel = selector.select();
            if (readChannel == 0) {
                continue;
            }
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey sk = iter.next();
                if (sk.isAcceptable()) {
                    handleAccept(channel, selector);
                } else if (sk.isReadable()) {
                    handleRead(sk, selector);
                }
                iter.remove();
            }
        }
    }

    private static void handleAccept(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        System.out.println("handle accept...");
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        channel.write(ByteBuffer.wrap("你已进入房间了".getBytes()));

    }

    private static void handleRead(SelectionKey key, Selector selector) throws IOException {
        System.out.println("可以读取数据了");
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        int i = channel.read(buffer);
        if (i != -1) {
            String msg = new String(buffer.array()).trim();
            System.out.println("server received message: " + msg);
            System.out.println("server reply: " + msg);
            channel.register(selector, SelectionKey.OP_READ);
            channel.write(ByteBuffer.wrap(msg.getBytes()));
        } else {
            channel.close();
        }

    }

    private static void handleConnect() {
        System.out.println("连接了");
    }
}

package com.muchen.web.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ClassName Container
 * Description TODO
 * Author fuyanzhang
 * Date 2020/9/18 11:43
 **/
public class Container {
    private ServerSocketChannel serverSocketChannel = null;
    private final ConcurrentLinkedQueue<MuchenChannel> events =
            new ConcurrentLinkedQueue<>();

    public void init(int port) throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        //绑定
        serverSocketChannel.bind(new InetSocketAddress(port), 100);
        //超时时间
        serverSocketChannel.socket().setSoTimeout(6000);
        serverSocketChannel.configureBlocking(true);
    }

    public void register(SocketChannel channel) throws IOException {
        System.out.println("put ....");
        MuchenChannel muchenChannel = new MuchenChannel();
        channel.configureBlocking(false);
        muchenChannel.setChannel(channel);
        muchenChannel.setType(MuchenChannelType.REGISTER);
        events.add(muchenChannel);
    }

    public SocketChannel getSocketChannel() throws IOException {
        return this.serverSocketChannel.accept();
    }

    public ConcurrentLinkedQueue<MuchenChannel> getEvents() {
        return events;
    }

}

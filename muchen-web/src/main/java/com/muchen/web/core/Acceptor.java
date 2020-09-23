package com.muchen.web.core;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * ClassName Acceptor
 * Description TODO
 * Author fuyanzhang
 * Date 2020/9/18 15:32
 **/

public class Acceptor implements Runnable {
    private Container container;

    public Acceptor(Container container) {
        this.container = container;
    }

    @Override
    public void run() {
        while (true) {
            try {
                SocketChannel channel = container.getSocketChannel();
                if (channel != null) {
                    container.register(channel);
                } else {
                    Thread.sleep(50);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

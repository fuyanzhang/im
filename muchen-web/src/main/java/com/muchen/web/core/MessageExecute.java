package com.muchen.web.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * ClassName MessageExecute
 * Description TODO
 * Author fuyanzhang
 * Date 2020/9/18 16:37
 **/

public class MessageExecute implements Runnable {
    private SocketChannel channel;

    public MessageExecute(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void run() {
        System.out.println("接收到数据");
        ByteBuffer buffer = ByteBuffer.allocate(256);
        int i = 0;
        try {
            i = channel.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (i != -1) {
            String msg = new String(buffer.array()).trim();
            System.out.println("server received message: " + msg);
        }

        //写返回数据
        String reply = "我收到了，我不是帅哥";
        ByteBuffer repBuffer = ByteBuffer.allocate(256);
        repBuffer.clear();
        repBuffer.put(reply.getBytes());
        repBuffer.flip();
        try {
            while (repBuffer.hasRemaining()) {
                channel.write(repBuffer);
            }
            channel.close();
            repBuffer.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

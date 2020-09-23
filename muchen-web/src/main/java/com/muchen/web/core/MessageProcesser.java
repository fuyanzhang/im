package com.muchen.web.core;

import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName MessageProcesser
 * Description TODO
 * Author fuyanzhang
 * Date 2020/9/18 15:40
 **/
public class MessageProcesser implements Runnable {

    private Container container;
    private Selector selector;

    private ExecutorService executor = new ThreadPoolExecutor(128, 128, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(512), new ThreadFactory() {
        private final AtomicInteger threadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "muchen-exec-" + threadNum.getAndIncrement());
            t.setDaemon(true);
            return t;
        }
    }, new RejectedExecutionHandler() {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("reject");
        }
    });

    public MessageProcesser(Container container) throws IOException {
        this.container = container;
        selector = Selector.open();
    }

    @Override
    public void run() {

        while (true) {
            try {
                if (CollectionUtils.isEmpty(container.getEvents())) {
                    Thread.sleep(50);
                    continue;
                }
                ConcurrentLinkedQueue<MuchenChannel> events = container.getEvents();
                MuchenChannel mc = null;
                for (int i = 0; i < events.size() && (mc = events.poll()) != null; i++) {
                    if (mc.getType() == MuchenChannelType.REGISTER) {
                        mc.getChannel().register(selector, SelectionKey.OP_READ, mc.getChannel());
                    }
                }
                int readChannel = selector.select();
                if (readChannel == 0) {
                    System.out.println("xxxxxxxxxxxxx");
                    continue;
                }
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey sk = iter.next();
                    if (sk.isReadable()) {
                        iter.remove();
                        process(sk);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void process(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();

        MessageExecute execute = new MessageExecute(channel);
        executor.submit(execute);

    }
}

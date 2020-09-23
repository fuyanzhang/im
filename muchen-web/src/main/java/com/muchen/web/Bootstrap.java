package com.muchen.web;

import com.muchen.web.core.Acceptor;
import com.muchen.web.core.Container;
import com.muchen.web.core.MessageProcesser;

import java.io.IOException;

/**
 * ClassName Bootstrap
 * Description TODO
 * Author fuyanzhang
 * Date 2020/9/17 18:51
 **/
public class Bootstrap {

    public static void main(String[] args) throws IOException, InterruptedException {

        Container container = new Container();
        container.init(8144);
        Acceptor acceptor = new Acceptor(container);
        MessageProcesser processer = new MessageProcesser(container);
        Thread at = new Thread(acceptor);
        at.setDaemon(true);
        at.setName("acceptor");
        at.start();

        Thread pt = new Thread(processer);
        pt.setName("message-processer");
        pt.setDaemon(true);
        pt.start();

        at.join();
        pt.join();

    }
}

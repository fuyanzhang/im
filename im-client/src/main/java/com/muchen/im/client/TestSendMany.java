package com.muchen.im.client;

/**
 * ClassName TestSendMany
 * Description TODO
 * Author fuyanzhang
 * Date 2020/9/23 15:21
 **/
public class TestSendMany {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5000; i++) {
            Thread t = new Thread(new SendTcp());
            t.start();
            t.join();

        }
    }
}

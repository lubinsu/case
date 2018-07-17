package com.maiya.util.solr;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lubin 2016/11/23
 */
public class SynchronizedTest {

    public SynchronizedTest() {
        Thread task1 = new TimerSendTask();
        task1.start();
    }

    public synchronized void sendBatch(String str, int timewait) throws InterruptedException {
        System.out.println("sending..." + str);
        Thread.sleep(timewait);
        System.out.println("end ......" + str);
    }

    /**
     * 定时同步任务
     */
    private class TimerSendTask extends Thread {
        public void run() {
            Timer timer = new Timer();
            timer.schedule(new SendTask(), 1000, 1000);
        }
    }

    class SendTask extends TimerTask {
        public void run() {
            try {
                sendBatch("timmer", 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String string, int timewait) throws InterruptedException {
        sendBatch(string, timewait);
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTest test = new SynchronizedTest();
        test.send("hello", 1000);
        test.send("hi", 10000);
    }
}

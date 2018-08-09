package com.maiya.threads;

import java.util.concurrent.CountDownLatch;

/**
 * Created by lubinsu
 * Date: 2018/8/9 10:50
 * Desc:
 */
public class TestRunnable implements Runnable {
    private int time = 10000;
    private CountDownLatch countDownLatch;
    private SourceA s;
    private String id = "001";

    public TestRunnable(SourceA s, CountDownLatch countDownLatch) {
        this.s = s;
        this.countDownLatch = countDownLatch;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void run() {
        try {
            System.out.println(id + " sleep：" + time);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        synchronized (s) {
            s.notify();
            System.out.println("我NOTIFY了：" + id);
            System.out.println("我存入了id：" + id);
            s.setSource(id);
        }
        countDownLatch.countDown();
    }
}

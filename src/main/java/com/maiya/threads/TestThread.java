package com.maiya.threads;

import java.util.concurrent.CountDownLatch;

/**
 * Created by lubinsu
 * Date: 2018/8/9 10:52
 * Desc:
 */
public class TestThread extends Thread {
    private int time = 5000;
    private SourceA s;
    private CountDownLatch countDownLatch;
    String id = "002";

    public void setTime(int time) {
        this.time = time;
    }

    public TestThread(SourceA s, CountDownLatch countDownLatch) {
        this.s = s;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            System.out.println(id + " sleep：" + time);
            sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        synchronized (s) {
            try {
                System.out.println("我" + id + "要进行等待了");
                s.wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("我被唤醒了：" + id);
            System.out.println("我存入了id：" + id);
            s.setSource(id);
        }
        countDownLatch.countDown();
    }
}

package com.maiya.jvm;

/**
 * Created by lubinsu
 * Date: 2018/7/20 16:42
 * Desc: 死循环Runnable线程导致的cpu消耗直线上升
 * 使用ps -ef | grep java | grep -v grep找出java进程
 * 使用top -Hp pid 找出最消耗CPU的线程
 * 假设线程ID为21742：printf "%x\n" 21742 得到21742的十六进制值为54ee
 * stack 21711 | grep 54ee 就知道是哪个线程最消耗CPU了
 * "PollIntervalRetrySchedulerThread" prio=10 tid=0x00007f950043e000 nid=0x54ee in Object.wait()
 */
public class DumpWhileDemo {

    public static void main(String[] args) {
        new Thread(new WhileThread()).start();
        System.out.println();
    }
}

class WhileThread implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("Thread");
        }
    }
}

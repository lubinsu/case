package com.maiya.util.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * Created by lubin 2016/11/22
 */
public class ZookeeperTool implements Watcher {

    private ZooKeeper zooKeeper;
    private static final int SESSION_TIME_OUT = 2000;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent event) {

        if (event.getState() == Event.KeeperState.SyncConnected) {
            System.out.println("Watch received event");
            countDownLatch.countDown();
        }
    }

    /**
     * 连接zookeeper
     *
     * @param host 主机
     * @throws Exception
     */
    public void connectZookeeper(String host) throws Exception {
        zooKeeper = new ZooKeeper(host, SESSION_TIME_OUT, this);
        countDownLatch.await();
        System.out.println("zookeeper connection success");
    }

    /**
     * 获取节点上面的数据
     *
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public String getData(String path) throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData(path, false, null);
        if (data == null) {
            return "";
        }
        return new String(data);
    }

    public static void main(String[] args) throws Exception {

        ZookeeperTool tool = new ZookeeperTool();
        tool.connectZookeeper("192.168.0.113,192.168.0.114,192.168.0.115");

        System.out.println(tool.getData("/consumers/lubinsu_test/offsets/hy_membersms/0"));

    }
}

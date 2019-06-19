package com.wvkia.tools.kiwi.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @author wukai
 * @date 2019/5/29
 */
public class ZKConstructUseageSimple implements Watcher {
    @Override
    public void process(WatchedEvent watchedEvent) {

    }

    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:3000", 50000, new ZKConstructUseageSimple());
        zooKeeper.getData();
        Packe
    }
}

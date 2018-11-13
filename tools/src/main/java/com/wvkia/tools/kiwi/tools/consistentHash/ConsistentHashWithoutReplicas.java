package com.wvkia.tools.kiwi.tools.consistentHash;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;

public class ConsistentHashWithoutReplicas<T> {
    //用的hash函数
    private HashFunction hashFunction;

    //环状节点分布圆
    private final SortedMap<Integer, T> circle = Maps.newTreeMap();

    public ConsistentHashWithoutReplicas(HashFunction hashFunction, Collection<T> circle ) {
        this.hashFunction = hashFunction;
        for (T t : circle) {
            add(t);
        }
    }

    /**
     * 加入server节点
     * @param node
     */
    public void add(T node) {
        circle.put(hashFunction.hashString(node.toString(),Charsets.UTF_8).asInt(), node);
    }

    public void remove(T node) {
        circle.remove(hashFunction.hashString(node.toString(), Charsets.UTF_8).asInt());
    }

    /**
     * 获取client对应server节点
     * @param key
     * @return
     */
    public T get(Object key) {
        if (circle.isEmpty()) {
            return null;
        }

        //生成client对应的hash值
        int hash = hashFunction.hashString(key.toString(), Charsets.UTF_8).asInt();

        //如果没有对应此hash的server节点，获取大于等于此hash后面的server节点；如果还没有，则获取server节点分布圆的第一个节点
        if (!circle.containsKey(hash)) {

            //返回大于等于此hash后面的节点
            SortedMap<Integer, T> tailMap = circle.tailMap(hash);

            //如果还没有，则获取server节点分布圆的第一个节点
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }

    public static void main(String[] args) {
        ArrayList<String> nodeList = new ArrayList<String>();
        nodeList.add("www.google.com.hk");
        nodeList.add("www.apple.com.cn");
        nodeList.add("twitter.com");
        nodeList.add("weibo.com");

        HashFunction hf = Hashing.md5();

        ConsistentHashWithoutReplicas<String> consistentHashWithoutReplicas = new ConsistentHashWithoutReplicas<String>(hf, nodeList);

        //circle圆分布
        for (Integer i : consistentHashWithoutReplicas.circle.keySet()) {
            System.out.print(""+i+":"+ consistentHashWithoutReplicas.circle.get(i)+" ==> " );
        }
        System.out.println();
        //根据一致性hash算法获取客户端对应的服务器节点
        String string=RandomStringUtils.random(12);
        int hash = hf.hashString(string, Charsets.UTF_8).asInt();
        System.out.println("hash value == " + hash);
        System.out.println("hash send to == "+ consistentHashWithoutReplicas.get(string));
    }
}

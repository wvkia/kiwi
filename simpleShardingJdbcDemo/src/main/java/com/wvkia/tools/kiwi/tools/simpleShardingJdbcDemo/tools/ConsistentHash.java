package com.wvkia.tools.kiwi.tools.simpleShardingJdbcDemo.tools;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;

/**
 * 使用
 * @param <T>
 */
public class ConsistentHash<T> {

    //用的hash函数
//    private HashFunction hashFunction;

    private static MessageDigest md5;

    // server虚拟节点倍数(100左右比较合理)
    private final int  numberOfReplicas;


    //环状节点分布圆
    private final SortedMap<Integer, T> circle = Maps.newTreeMap();

    public ConsistentHash(int numberOfReplicas, Collection<T> circle ) {
        this.numberOfReplicas = numberOfReplicas;
        for (T t : circle) {
            add(t);
        }
    }

    public  SortedMap getCircle(){
        return circle;
    }

    /**
     * 加入server节点
     * @param node
     */
    public void add(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.put(Hash(node.toString()), node);
        }
    }

    public void remove(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            circle.remove(Hash(node.toString()));
        }
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
        int hash = Hash(key.toString());

        //如果没有对应此hash的server节点，获取大于等于此hash后面的server节点；如果还没有，则获取server节点分布圆的第一个节点
        if (!circle.containsKey(hash)) {

            //返回大于等于此hash后面的节点
            SortedMap<Integer, T> tailMap = circle.tailMap(hash);

            //如果还没有，则获取server节点分布圆的第一个节点
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }
    /**
     * ketama算法
     * @param key
     * @return
     */
    public int Hash(String key) {
        if (md5 == null) {
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalArgumentException("no md5 algorythm found");
            }
        }
        md5.reset();
        md5.update(key.getBytes());
        byte[] bKey = md5.digest();

        long res = ((long) (bKey[3] & 0xFF) << 24)
                | ((long) (bKey[2] & 0xFF) << 16)
                | ((long) (bKey[1] & 0xFF) << 8)
                | (bKey[0] & 0xFF);
        return (int) res;
    }

    public static void main(String[] args) {
        ArrayList<String> nodeList = new ArrayList<String>();
        nodeList.add("www.google.com.hk");
        nodeList.add("www.apple.com.cn");
        nodeList.add("twitter.com");
        nodeList.add("weibo.com");

        HashFunction hf = Hashing.md5();

        ConsistentHash<String> consistentHash = new ConsistentHash<String>(5, nodeList);

        //circle圆分布
        for (Integer i : consistentHash.circle.keySet()) {
            System.out.print(""+i+":"+ consistentHash.circle.get(i)+" ==> " );
        }
        System.out.println();
        //根据一致性hash算法获取客户端对应的服务器节点
        String string=RandomStringUtils.random(12);
        int hash = hf.hashString(string, Charsets.UTF_8).asInt();
        System.out.println("hash value == " + hash);
        System.out.println("hash send to == "+ consistentHash.get(string));
    }
}

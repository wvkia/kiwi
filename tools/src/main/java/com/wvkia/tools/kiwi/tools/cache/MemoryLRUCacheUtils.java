package com.wvkia.tools.kiwi.tools.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wukai
 * 利用LinkedHashMap构造的最近最久未使用内存cache
 */
public class MemoryLRUCacheUtils<K,V> {

    private LRULinkedHashMap<K, V> lruLinkedHashMap;

    private int MaxCapacity=200;

    private MemoryLRUCacheUtils(){
        lruLinkedHashMap = new LRULinkedHashMap<>(MaxCapacity);
    }

    private MemoryLRUCacheUtils(int capacity){
        lruLinkedHashMap = new LRULinkedHashMap<>(capacity);
    }

    public static MemoryLRUCacheUtils build() {

        return new MemoryLRUCacheUtils();
    }

    public static MemoryLRUCacheUtils build(int maxCapacity) {
        return new MemoryLRUCacheUtils(maxCapacity);
    }

    public void put(K key,V value) {
        lruLinkedHashMap.put(key, value);
    }

    public  V get(K key) {
       return lruLinkedHashMap.get(key);
    }

    public boolean containKey(K key) {
        return lruLinkedHashMap.containsKey(key);
    }


    private class LRULinkedHashMap<K,V> extends LinkedHashMap<K,V> {
        private int capacity;
        private static final long serialVersionUID = 1L;
        LRULinkedHashMap(int capacity){
            super(16,0.75f,true);
            this.capacity=capacity;
        }
        @Override
        public boolean removeEldestEntry(Map.Entry<K, V> eldest){
            return size()>capacity;
        }
    }

}

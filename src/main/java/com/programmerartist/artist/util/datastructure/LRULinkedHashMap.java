package com.programmerartist.artist.util.datastructure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程安全的 LRU map
 *
 * Created by 程序员Artist on 16/3/25.
 */
public class LRULinkedHashMap<K,V> extends LinkedHashMap<K,V> {

    private final int maxCapacity;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * 线程安全保证
     *
     * 不能用读写锁，否则也会报并发错误
     *
     */
    private Lock lock;

    /**
     * 构造函数，指定最大容量值；自动生成锁对象
     *
     * @param maxCapacity
     */
    public LRULinkedHashMap(int maxCapacity) {
        super(maxCapacity, DEFAULT_LOAD_FACTOR, true);

        this.maxCapacity = maxCapacity;
        lock = new ReentrantLock();
    }

    /**
     * 实现LRU的关键方法，如果map里面的元素个数大于了缓存最大容量，则删除链表的顶端元素
     *
     * @param
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
    {
        return size() > maxCapacity;
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public V get(Object key) {
        try {
            lock.lock();

            return super.get(key);

        } finally {
            lock.unlock();
        }
    }

    /**
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public V put(K key, V value) {
        try {
            lock.lock();

            return super.put(key, value);

        } finally {
            lock.unlock();
        }
    }

    /**
     *
     * @param m
     */
    public void putAll(Map<? extends K, ? extends V> m){
        try {
            lock.lock();

            super.putAll(m);

        } finally {
            lock.unlock();
        }

    }

    /**
     * 复制出来一个，以保证线程安全
     *
     * @return
     */
    public Set<K> keySet() {

        try {
            lock.lock();

            return new HashSet<>(super.keySet());

        } finally {
            lock.unlock();
        }
    }

    /**
     * 复制出来一个，以保证线程安全
     *
     * @return
     */
    public Set<Map.Entry<K,V>> entrySet() {

        try {
            lock.lock();

            return new HashSet<>(super.entrySet());

        } finally {
            lock.unlock();
        }
    }

}

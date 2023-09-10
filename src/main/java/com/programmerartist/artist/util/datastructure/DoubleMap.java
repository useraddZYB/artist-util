package com.programmerartist.artist.util.datastructure;

import java.util.Map;

/**
 * Created by 程序员Artist on 2016/12/14.
 */
public interface DoubleMap<V> {

    /**
     *
     * @param key
     * @param value
     */
    void put(String key, Map<String, V> value);

    /**
     *
     * @param key
     * @param keyInner
     * @param value
     */
    void put(String key, String keyInner, V value);

    /**
     *
     * @param key
     * @return
     */
    boolean containsKey(String key);

    /**
     *
     * @param key
     * @param keyInner
     * @return
     */
    boolean containsKey(String key, String keyInner);

    /**
     *
     * @param key
     * @return
     */
    Map<String, V> get(String key);

    /**
     *
     * @param key
     * @param keyInner
     * @return
     */
    V get(String key, String keyInner);

    /**
     *
     * @param key
     */
    void remove(String key);

    /**
     *
     * @param key
     * @param keyInner
     */
    void remove(String key, String keyInner);

    /**
     *
     * @return
     */
    int size();

    /**
     *
     * @param key
     * @return
     */
    int size(String key);


    /**
     *
     * @return
     */
    Map<String, Map<String, V>> innerMap();

}

package com.programmerartist.artist.util.datastructure;

import com.programmerartist.artist.util.common.ParamUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 双重map
 *
 * 提供更适用的api，简化代码开发
 *
 * Map<String, Map<String, V>>
 *
 * Created by 程序员Artist on 2016/12/14.
 */
public class DoubleHashMap<V> implements DoubleMap<V> {

    private final Map<String, Map<String, V>> container = new HashMap<>();

    /**
     *
     * @param key
     * @param value
     */
    @Override
    public void put(String key, Map<String, V> value) {
        ParamUtil.assertBlank(key);
        ParamUtil.assertNull(value);

        container.put(key, value);
    }

    /**
     *
     * @param key
     * @param keyInner
     * @param value
     */
    @Override
    public void put(String key, String keyInner, V value) {
        ParamUtil.assertBlank(key, keyInner, value);

        if(!container.containsKey(key)) {
            container.put(key, new HashMap<>());
        }

        container.get(key).put(keyInner, value);
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(String key) {
        return container.containsKey(key);
    }

    /**
     *
     * @param key
     * @param keyInner
     * @return
     */
    @Override
    public boolean containsKey(String key, String keyInner) {
        return container.containsKey(key) && container.get(key).containsKey(keyInner);
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public Map<String, V> get(String key) {
        return container.get(key);
    }

    /**
     *
     * @param key
     * @param keyInner
     * @return
     */
    @Override
    public V get(String key, String keyInner) {
        return container.containsKey(key) ? container.get(key).get(keyInner) : null;
    }

    /**
     *
     * @param key
     */
    @Override
    public void remove(String key) {
        container.remove(key);
    }

    /**
     *
     * @param key
     * @param keyInner
     */
    @Override
    public void remove(String key, String keyInner) {
        if(container.containsKey(key)) {
            container.get(key).remove(keyInner);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public int size() {
        return container.size();
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public int size(String key) {
        return container.containsKey(key) && !ParamUtil.isBlank(container.get(key)) ? container.get(key).size() : 0;
    }

    /**
     *
     * @return
     */
    @Override
    public Map<String, Map<String, V>> innerMap() {
        return container;
    }
}

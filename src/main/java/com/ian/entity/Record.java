package com.ian.entity;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ianhau
 */
public final class Record implements Map<String, Object> {
    private final Map<String, Object> map = new HashMap<>();

    Record() {
    }

    public static Record fromMap(Map result) {
        Record r = new Record();
        r.putAll(result);
        return r;
    }

    void setValue(String key, Object value) {
        this.map.put(key, value);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsValue(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return this.map.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return this.map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public void putAll(@Nonnull Map<? extends String, ?> m) {
        this.map.putAll(m);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    @Nonnull
    public Set<String> keySet() {
        return this.map.keySet();
    }

    @Override
    @Nonnull
    public Collection<Object> values() {
        return this.map.values();
    }

    @Override
    @Nonnull
    public Set<Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

}

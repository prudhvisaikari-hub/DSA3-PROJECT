package com.texthackplus.structures.map;

/**
 * Very simple hash map implementation using open addressing (linear probing).
 * No java.util classes are used. Designed to store moderate‑size mappings
 * such as term → posting‑list for the document index.
 *
 * @param <K> key type – must provide a reliable {@code hashCode()} and
 *            {@code equals()} implementation.
 * @param <V> value type.
 */
public class CustomHashMap<K, V> {
    private static class Entry<K, V> {
        final K key;
        V value;
        boolean deleted; // tombstone for removed entries
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.deleted = false;
        }
    }

    private Entry<K, V>[] table;
    private int size; // number of active entries
    private int capacity;
    private static final float LOAD_FACTOR = 0.7f;
    private static final int INITIAL_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        this.capacity = INITIAL_CAPACITY;
        this.table = (Entry<K, V>[]) new Entry[capacity];
        this.size = 0;
    }

    /** Returns number of key‑value pairs stored. */
    public int size() {
        return size;
    }

    /** Returns all active keys in the map. */
    public com.texthackplus.structures.array.DynamicArray<K> keySet() {
        com.texthackplus.structures.array.DynamicArray<K> keys = new com.texthackplus.structures.array.DynamicArray<>();
        for (int i = 0; i < capacity; i++) {
            Entry<K, V> e = table[i];
            if (e != null && !e.deleted) {
                keys.add(e.key);
            }
        }
        return keys;
    }

    /** Computes index for a key using its hashCode. */
    private int indexFor(Object key, int cap) {
        int h = key == null ? 0 : key.hashCode();
        // spread bits (similar to Java's HashMap) and ensure non‑negative
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % cap;
    }

    /** Retrieves the value for {@code key}, or {@code null} if absent. */
    public V get(K key) {
        int idx = indexFor(key, capacity);
        while (true) {
            Entry<K, V> e = table[idx];
            if (e == null) {
                return null;
            }
            if (!e.deleted && (key == null ? e.key == null : key.equals(e.key))) {
                return e.value;
            }
            idx = (idx + 1) % capacity; // linear probe
        }
    }

    /** Associates {@code key} with {@code value}, overwriting any existing mapping. */
    public void put(K key, V value) {
        if (size + 1 > capacity * LOAD_FACTOR) {
            resize();
        }
        int idx = indexFor(key, capacity);
        while (true) {
            Entry<K, V> e = table[idx];
            if (e == null || e.deleted) {
                table[idx] = new Entry<>(key, value);
                size++;
                return;
            }
            if (key == null ? e.key == null : key.equals(e.key)) {
                e.value = value; // replace existing
                return;
            }
            idx = (idx + 1) % capacity;
        }
    }

    /** Removes mapping for {@code key} if present. */
    public void remove(K key) {
        int idx = indexFor(key, capacity);
        while (true) {
            Entry<K, V> e = table[idx];
            if (e == null) {
                return; // not found
            }
            if (!e.deleted && (key == null ? e.key == null : key.equals(e.key))) {
                e.deleted = true; // tombstone
                size--;
                return;
            }
            idx = (idx + 1) % capacity;
        }
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCap = capacity * 2;
        Entry<K, V>[] oldTable = table;
        table = (Entry<K, V>[]) new Entry[newCap];
        int oldCap = capacity;
        capacity = newCap;
        size = 0;
        for (int i = 0; i < oldCap; i++) {
            Entry<K, V> e = oldTable[i];
            if (e != null && !e.deleted) {
                put(e.key, e.value);
            }
        }
    }
}

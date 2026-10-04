package texthack.datastructures;

/**
 * Hash map from scratch (separate chaining) - no java.util.HashMap.
 * Used for the inverted index (word -> posting list) and other lookups.
 */
public class MyHashMap<K, V> {

    private static class Entry<K, V> {
        K key;
        V value;
        Entry(K key, V value) { this.key = key; this.value = value; }
    }

    private MyLinkedList<Entry<K, V>>[] buckets;
    private int capacity;
    private int count;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        capacity = 16;
        buckets = new MyLinkedList[capacity];
    }

    private int indexFor(K key) {
        int h = (key == null) ? 0 : key.hashCode();
        h ^= (h >>> 16);
        return Math.abs(h) % capacity;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        MyLinkedList<Entry<K, V>>[] old = buckets;
        capacity *= 2;
        buckets = new MyLinkedList[capacity];
        count = 0;
        for (MyLinkedList<Entry<K, V>> bucket : old) {
            if (bucket == null) continue;
            MyLinkedList.Node<Entry<K, V>> node = bucket.head();
            while (node != null) {
                put(node.value.key, node.value.value);
                node = node.next;
            }
        }
    }

    public void put(K key, V value) {
        if ((double) (count + 1) / capacity > LOAD_FACTOR) resize();
        int idx = indexFor(key);
        if (buckets[idx] == null) buckets[idx] = new MyLinkedList<>();
        MyLinkedList.Node<Entry<K, V>> node = buckets[idx].head();
        while (node != null) {
            if (node.value.key == null ? key == null : node.value.key.equals(key)) {
                node.value.value = value;
                return;
            }
            node = node.next;
        }
        buckets[idx].addLast(new Entry<>(key, value));
        count++;
    }

    public V get(K key) {
        int idx = indexFor(key);
        if (buckets[idx] == null) return null;
        MyLinkedList.Node<Entry<K, V>> node = buckets[idx].head();
        while (node != null) {
            if (node.value.key == null ? key == null : node.value.key.equals(key)) return node.value.value;
            node = node.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        int idx = indexFor(key);
        if (buckets[idx] == null) return false;
        MyLinkedList.Node<Entry<K, V>> node = buckets[idx].head();
        while (node != null) {
            if (node.value.key == null ? key == null : node.value.key.equals(key)) return true;
            node = node.next;
        }
        return false;
    }

    public int size() { return count; }

    public MyArrayList<K> keys() {
        MyArrayList<K> result = new MyArrayList<>();
        for (MyLinkedList<Entry<K, V>> bucket : buckets) {
            if (bucket == null) continue;
            MyLinkedList.Node<Entry<K, V>> node = bucket.head();
            while (node != null) {
                result.add(node.value.key);
                node = node.next;
            }
        }
        return result;
    }
}

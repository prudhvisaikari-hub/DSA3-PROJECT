package texthack.datastructures;

/**
 * Singly linked list, written from scratch. Used internally by MyHashMap
 * for separate-chaining buckets and anywhere O(1) head insert is needed.
 */
public class MyLinkedList<T> {

    public static class Node<T> {
        public T value;
        public Node<T> next;
        public Node(T value) { this.value = value; }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public Node<T> head() { return head; }

    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        node.next = head;
        head = node;
        if (tail == null) tail = node;
        size++;
    }

    public boolean remove(T value) {
        Node<T> prev = null, cur = head;
        while (cur != null) {
            if (cur.value == null ? value == null : cur.value.equals(value)) {
                if (prev == null) head = cur.next; else prev.next = cur.next;
                if (cur == tail) tail = prev;
                size--;
                return true;
            }
            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    public boolean contains(T value) {
        Node<T> cur = head;
        while (cur != null) {
            if (cur.value == null ? value == null : cur.value.equals(value)) return true;
            cur = cur.next;
        }
        return false;
    }
}

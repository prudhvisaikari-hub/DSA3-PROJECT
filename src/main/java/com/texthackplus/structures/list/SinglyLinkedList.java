package com.texthackplus.structures.list;

/**
 * Simple singly linked list implementation without java.util.LinkedList.
 * Supports O(1) prepend and O(n) search/removal. Used for token chains
 * where occasional insertions at the head are required.
 */
public class SinglyLinkedList<T> {
    private static class Node<E> {
        E value;
        Node<E> next;
        Node(E value) { this.value = value; }
    }

    private Node<T> head;
    private int size;

    public SinglyLinkedList() {
        head = null;
        size = 0;
    }

    /** Prepends a new element to the list. */
    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        node.next = head;
        head = node;
        size++;
    }

    /** Returns the element at the given index (0‑based). O(n). */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
        }
        Node<T> cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur.value;
    }

    /** Returns the current size. */
    public int size() {
        return size;
    }

    /** Removes and returns the first element. */
    public T removeFirst() {
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }
        T val = head.value;
        head = head.next;
        size--;
        return val;
    }
}

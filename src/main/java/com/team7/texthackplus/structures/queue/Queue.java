package com.team7.texthackplus.structures.queue;

/**
 * Queue implementation based on DynamicArray (circular buffer).
 * Provides O(1) amortized enqueue and dequeue.
 */
public class Queue<T> {
    private com.team7.texthackplus.structures.array.DynamicArray<T> buffer;
    private int head; // index of next element to dequeue
    private int tail; // index where next element will be enqueued

    public Queue() {
        buffer = new com.team7.texthackplus.structures.array.DynamicArray<>();
        head = 0;
        tail = 0;
    }

    /** Enqueues an element at the tail. */
    public void enqueue(T element) {
        // Ensure enough capacity
        if (tail >= buffer.size()) {
            buffer.add(element);
        } else {
            buffer.set(tail, element);
        }
        tail++;
    }

    /** Dequeues the element at the head. */
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T elem = buffer.get(head);
        head++;
        // Optional: shrink when head catches up to reduce memory, but omitted for simplicity.
        return elem;
    }

    /** Returns true if no elements are present. */
    public boolean isEmpty() {
        return head == tail;
    }

    /** Returns current number of elements. */
    public int size() {
        return tail - head;
    }
}

package com.texthackplus.structures.heap;

/**
 * Simple max-heap (priority queue) built on DynamicArray.
 * Supports O(log n) insert and extract-max. Used in the scheduling demo
 * where jobs with highest priority are processed first.
 */
public class MaxHeap<T extends Comparable<T>> {
    private final com.texthackplus.structures.array.DynamicArray<T> heap;

    public MaxHeap() {
        heap = new com.texthackplus.structures.array.DynamicArray<>();
    }

    /** Returns number of elements in the heap. */
    public int size() {
        return heap.size();
    }

    /** Inserts a new element while maintaining heap property. */
    public void insert(T element) {
        heap.add(element);
        siftUp(heap.size() - 1);
    }

    /** Retrieves and removes the maximum element (root). */
    public T extractMax() {
        if (heap.size() == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        T max = heap.get(0);
        T last = heap.pop();
        if (heap.size() > 0) {
            heap.set(0, last);
            siftDown(0);
        }
        return max;
    }

    /** Returns the max element without removing it. */
    public T peek() {
        if (heap.size() == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap.get(0);
    }

    // ----- internal helpers -----
    private void siftUp(int idx) {
        while (idx > 0) {
            int parent = (idx - 1) / 2;
            if (heap.get(parent).compareTo(heap.get(idx)) >= 0) {
                break;
            }
            swap(parent, idx);
            idx = parent;
        }
    }

    private void siftDown(int idx) {
        int size = heap.size();
        while (true) {
            int left = 2 * idx + 1;
            int right = 2 * idx + 2;
            int largest = idx;
            if (left < size && heap.get(left).compareTo(heap.get(largest)) > 0) {
                largest = left;
            }
            if (right < size && heap.get(right).compareTo(heap.get(largest)) > 0) {
                largest = right;
            }
            if (largest == idx) {
                break;
            }
            swap(idx, largest);
            idx = largest;
        }
    }

    private void swap(int i, int j) {
        T tmp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, tmp);
    }
}

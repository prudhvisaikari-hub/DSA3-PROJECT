package com.texthackplus.structures.array;

/**
 * Simple dynamic array implementation without using java.util.ArrayList.
 * Provides O(1) amortized append and O(1) random access.
 * Used throughout the project for storing token streams and other sequential data.
 */
public class DynamicArray<T> {
    private Object[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    public DynamicArray() {
        this.data = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /** Returns the number of stored elements. */
    public int size() {
        return size;
    }

    /** Returns element at index, O(1). */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
        }
        return (T) data[index];
    }

    /** Appends element, resizing if needed. */
    public void add(T element) {
        ensureCapacity(size + 1);
        data[size++] = element;
    }

    /** Sets element at index, returning previous value. */
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
        }
        T old = (T) data[index];
        data[index] = element;
        return old;
    }

    /** Removes last element and returns it. */
    @SuppressWarnings("unchecked")
    public T pop() {
        if (size == 0) {
            throw new IllegalStateException("Array is empty");
        }
        T elem = (T) data[--size];
        data[size] = null; // help GC
        return elem;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > data.length) {
            int newCap = Math.max(data.length * 2, minCapacity);
            Object[] newArr = new Object[newCap];
            System.arraycopy(data, 0, newArr, 0, size);
            data = newArr;
        }
    }
}

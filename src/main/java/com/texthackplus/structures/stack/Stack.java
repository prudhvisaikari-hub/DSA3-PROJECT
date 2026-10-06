package com.texthackplus.structures.stack;

/**
 * Stack implementation built on top of DynamicArray.
 * Provides O(1) push/pop operations.
 */
public class Stack<T> {
    private final com.texthackplus.structures.array.DynamicArray<T> data;

    public Stack() {
        data = new com.texthackplus.structures.array.DynamicArray<>();
    }

    /** Pushes an element onto the stack. */
    public void push(T element) {
        data.add(element);
    }

    /** Pops the top element, throwing if empty. */
    public T pop() {
        if (data.size() == 0) {
            throw new IllegalStateException("Stack is empty");
        }
        return data.pop();
    }

    /** Peeks at the top element without removing it. */
    public T peek() {
        if (data.size() == 0) {
            throw new IllegalStateException("Stack is empty");
        }
        return data.get(data.size() - 1);
    }

    /** Returns current stack size. */
    public int size() {
        return data.size();
    }

    /** Returns true if the stack contains no elements. */
    public boolean isEmpty() {
        return data.size() == 0;
    }
}

package texthack.datastructures;

/** Array-backed stack, from scratch. Used by graph traversals and backtracking. */
public class MyStack<T> {
    private final MyArrayList<T> data = new MyArrayList<>();

    public void push(T item) { data.add(item); }

    public T pop() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        return data.remove(data.size() - 1);
    }

    public T peek() {
        if (isEmpty()) throw new RuntimeException("Stack is empty");
        return data.get(data.size() - 1);
    }

    public boolean isEmpty() { return data.isEmpty(); }
    public int size() { return data.size(); }
}

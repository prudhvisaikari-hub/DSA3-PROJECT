package texthack.datastructures;

/** FIFO queue, from scratch, backed by the custom linked list. Used by BFS in max-flow. */
public class MyQueue<T> {
    private final MyLinkedList<T> data = new MyLinkedList<>();

    public void enqueue(T item) { data.addLast(item); }

    public T dequeue() {
        if (isEmpty()) throw new RuntimeException("Queue is empty");
        MyLinkedList.Node<T> node = data.head();
        T value = node.value;
        data.remove(value); // removes first occurrence == head here since we dequeue FIFO order
        return value;
    }

    public boolean isEmpty() { return data.isEmpty(); }
    public int size() { return data.size(); }
}
